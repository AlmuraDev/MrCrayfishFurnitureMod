package com.mrcrayfish.furniture.util;

import net.minecraft.world.entity.Entity;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Makes the trampoline and diving board throw an entity up at a chosen speed.
 *
 * Since 26.x, landing works out the bounce after Block#fallOn from the block's bounce restitution and overwrites any speed fallOn set.
 * So fallOn works out the speed (as 1.20 did) and stores it here; the block's getBounceRestitution then turns it into the restitution
 * that gives that speed.
 */
public class BounceHelper
{
    private static final Map<Entity, Double> PENDING = Collections.synchronizedMap(new WeakHashMap<>());

    public static void prepareBounce(Entity entity, double upwardSpeed)
    {
        PENDING.put(entity, upwardSpeed);
    }

    public static float takeRestitution(Entity entity)
    {
        Double speed = PENDING.remove(entity);
        if(speed == null)
            return 0.0F;

        double fallingSpeed = -entity.getDeltaMovement().y;
        return fallingSpeed > 0.01 ? (float) (speed / fallingSpeed) : 0.0F;
    }
}
