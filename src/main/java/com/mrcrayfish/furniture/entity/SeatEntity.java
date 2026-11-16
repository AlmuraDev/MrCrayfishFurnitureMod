package com.mrcrayfish.furniture.entity;

import com.mrcrayfish.furniture.core.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * The invisible entity a player rides when sitting on a chair, sofa or bench. It goes away when its rider gets off or the seat is broken.
 *
 * Author: MrCrayfish
 */
public class SeatEntity extends Entity
{
    public SeatEntity(EntityType<? extends SeatEntity> type, Level level)
    {
        super(type, level);
        this.noPhysics = true;
    }

    private SeatEntity(Level level, BlockPos source, double yOffset, Direction direction)
    {
        this(ModEntities.SEAT.get(), level);
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
        this.setRot(direction.getOpposite().toYRot(), 0F);
    }

    @Override
    public void tick()
    {
        super.tick();
        if(!this.level().isClientSide())
        {
            if(this.getPassengers().isEmpty() || this.level().isEmptyBlock(this.blockPosition()))
            {
                this.remove(RemovalReason.DISCARDED);
                this.level().updateNeighbourForOutputSignal(this.blockPosition(), this.level().getBlockState(this.blockPosition()).getBlock());
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage)
    {
        return false;
    }

    // The rider sits where the seat is (the seat's height is set by the block that made it)
    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale)
    {
        return Vec3.ZERO;
    }

    @Override
    protected boolean canRide(Entity entity)
    {
        return true;
    }

    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player, Direction direction)
    {
        if(!level.isClientSide())
        {
            List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(pos));
            if(seats.isEmpty())
            {
                SeatEntity seat = new SeatEntity(level, pos, yOffset, direction);
                level.addFreshEntity(seat);
                player.startRiding(seat, false, true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity)
    {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};
        for(Direction dir : offsets)
        {
            Vec3 safeVec = DismountHelper.findSafeDismountLocation(entity.getType(), this.level(), this.blockPosition().relative(dir), false);
            if(safeVec != null)
            {
                return safeVec.add(0, 0.25, 0);
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }

    @Override
    protected void addPassenger(Entity entity)
    {
        super.addPassenger(entity);
        entity.setYRot(this.getYRot());
    }

    @Override
    public void positionRider(Entity entity, Entity.MoveFunction function)
    {
        super.positionRider(entity, function);
        this.clampYaw(entity);
    }

    @Override
    public void onPassengerTurned(Entity entity)
    {
        this.clampYaw(entity);
    }

    private void clampYaw(Entity passenger)
    {
        passenger.setYBodyRot(this.getYRot());
        float wrappedYaw = Mth.wrapDegrees(passenger.getYRot() - this.getYRot());
        float clampedYaw = Mth.clamp(wrappedYaw, -120.0F, 120.0F);
        passenger.yRotO += clampedYaw - wrappedYaw;
        passenger.setYRot(passenger.getYRot() + clampedYaw - wrappedYaw);
        passenger.setYHeadRot(passenger.getYRot());
    }
}
