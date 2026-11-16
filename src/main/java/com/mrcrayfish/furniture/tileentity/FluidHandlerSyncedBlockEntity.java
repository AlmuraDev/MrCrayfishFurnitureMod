package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.util.BlockEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * A block entity with a one-tank fluid store that the clients see (for rendering it). Other mods reach the tank through the fluid handler
 * capability (see CapabilitySetup).
 *
 * Author: MrCrayfish
 */
public abstract class FluidHandlerSyncedBlockEntity extends BlockEntity
{
    private static final String TANK_KEY = "Tank";

    protected final FluidStacksResourceHandler tank;

    public FluidHandlerSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity)
    {
        super(type, pos, state);
        this.tank = new FluidStacksResourceHandler(1, capacity)
        {
            @Override
            protected void onContentsChanged(int index, FluidStack previousContents)
            {
                FluidHandlerSyncedBlockEntity.this.setChanged();
                BlockEntityUtil.sendUpdatePacket(FluidHandlerSyncedBlockEntity.this);
            }
        };
    }

    public ResourceHandler<FluidResource> getFluidHandler()
    {
        return this.tank;
    }

    public FluidStack getFluid()
    {
        return this.tank.copyToList().getFirst();
    }

    public int getFluidAmount()
    {
        return this.tank.getAmountAsInt(0);
    }

    public int getCapacity()
    {
        return this.tank.getCapacityAsInt(0, FluidResource.EMPTY);
    }

    /**
     * Adds fluid to the tank.
     *
     * @return the amount added
     */
    public int fill(Fluid fluid, int amount)
    {
        try(Transaction transaction = Transaction.openRoot())
        {
            int filled = this.tank.insert(0, FluidResource.of(fluid), amount, transaction);
            transaction.commit();
            return filled;
        }
    }

    /**
     * Takes fluid out of the tank, whatever it holds.
     *
     * @return the amount taken
     */
    public int drain(int amount)
    {
        FluidResource resource = this.tank.getResource(0);
        if(resource.isEmpty())
            return 0;

        try(Transaction transaction = Transaction.openRoot())
        {
            int drained = this.tank.extract(0, resource, amount, transaction);
            transaction.commit();
            return drained;
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        input.child(TANK_KEY).ifPresent(this.tank::deserialize);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        this.tank.serialize(output.child(TANK_KEY));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
