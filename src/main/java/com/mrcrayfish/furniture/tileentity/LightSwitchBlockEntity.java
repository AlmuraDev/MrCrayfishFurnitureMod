package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.Switchable;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import com.mrcrayfish.furniture.core.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * The lights a placed light switch turns on and off. They come from the switch item it was placed from (linked by using the item on
 * the lights) and go back onto the item it drops (from 1.12.2).
 *
 * Author: MrCrayfish
 */
public class LightSwitchBlockEntity extends BlockEntity
{
    private static final String LIGHTS_KEY = "Lights";

    private final List<BlockPos> lights = new ArrayList<>();

    public LightSwitchBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.LIGHT_SWITCH.get(), pos, state);
    }

    /**
     * Turns every linked light on or off, forgetting the ones that have since been broken.
     */
    public void switchLights(boolean powered)
    {
        if(this.level == null)
            return;

        boolean removed = this.lights.removeIf(lightPos -> this.level.isLoaded(lightPos) && !(this.level.getBlockState(lightPos).getBlock() instanceof Switchable));
        for(BlockPos lightPos : this.lights)
        {
            if(this.level.isLoaded(lightPos))
            {
                BlockState state = this.level.getBlockState(lightPos);
                if(state.getBlock() instanceof Switchable switchable)
                {
                    switchable.setPowered(this.level, lightPos, state, powered);
                }
            }
        }
        if(removed)
        {
            this.setChanged();
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components)
    {
        super.applyImplicitComponents(components);
        this.lights.clear();
        this.lights.addAll(components.getOrDefault(ModDataComponents.LINKED_LIGHTS.get(), List.of()));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        if(!this.lights.isEmpty())
        {
            components.set(ModDataComponents.LINKED_LIGHTS.get(), List.copyOf(this.lights));
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output)
    {
        super.removeComponentsFromTag(output);
        output.discard(LIGHTS_KEY);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.lights.clear();
        input.read(LIGHTS_KEY, BlockPos.CODEC.listOf()).ifPresent(this.lights::addAll);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store(LIGHTS_KEY, BlockPos.CODEC.listOf(), List.copyOf(this.lights));
    }
}
