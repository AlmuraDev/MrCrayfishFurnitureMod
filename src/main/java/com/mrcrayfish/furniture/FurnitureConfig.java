package com.mrcrayfish.furniture;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: MrCrayfish
 */
public class FurnitureConfig
{
    public static class Common
    {
        public final ModConfigSpec.IntValue maxMailQueue;
        public final ModConfigSpec.IntValue pullMailInterval;

        Common(ModConfigSpec.Builder builder)
        {
            builder.comment("Mail configuration settings").push("mail");
            this.maxMailQueue = builder
                    .comment("The maximum amount of mail that can be in a player's mail queue.")
                    .translation("cfm.configgui.maxMailQueue")
                    .defineInRange("maxMailQueue", 20, 1, Integer.MAX_VALUE);
            this.pullMailInterval = builder
                    .comment("The interval in ticks for mail boxes to pull mail from the player's queue")
                    .translation("cfm.configgui.pullMailInterval")
                    .defineInRange("pullMailInterval", 20, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    static final ModConfigSpec commonSpec;
    public static final FurnitureConfig.Common COMMON;

    static
    {
        final Pair<FurnitureConfig.Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(FurnitureConfig.Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
