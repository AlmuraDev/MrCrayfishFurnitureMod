package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.block.CeilingFanBlock;
import com.mrcrayfish.furniture.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * How fast a ceiling fan's blades turn (client only): they speed up to full speed while it is on and slow to a stop when it is off,
 * as in 1.12.2. Whether it is on is in the block state.
 *
 * Author: MrCrayfish
 */
public class CeilingFanBlockEntity extends BlockEntity
{
    private static final float MAX_SPEED = 30F;
    private static final float ACCELERATION = 0.25F;

    private float rotation;
    private float previousRotation;
    private float speed;

    public CeilingFanBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CEILING_FAN.get(), pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CeilingFanBlockEntity fan)
    {
        fan.previousRotation = fan.rotation;
        if(state.getValue(CeilingFanBlock.POWERED))
        {
            fan.speed = Math.min(MAX_SPEED, fan.speed + ACCELERATION);
        }
        else
        {
            fan.speed *= 0.95F;
        }
        fan.rotation = (fan.rotation + fan.speed) % 360F;
        if(fan.rotation < fan.previousRotation)
        {
            fan.previousRotation -= 360F;
        }
    }

    public float getRotation(float partialTicks)
    {
        return this.previousRotation + (this.rotation - this.previousRotation) * partialTicks;
    }
}
