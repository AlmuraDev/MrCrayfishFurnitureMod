package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModSounds
{
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, Reference.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_CABINET_OPEN = register("block.cabinet.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_CABINET_CLOSE = register("block.cabinet.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BEDSIDE_CABINET_OPEN = register("block.bedside_cabinet.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BEDSIDE_CABINET_CLOSE = register("block.bedside_cabinet.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BLINDS_OPEN = register("block.blinds.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BLINDS_CLOSE = register("block.blinds.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TRAMPOLINE_BOUNCE = register("block.trampoline.bounce");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_GRILL_PLACE = register("block.grill.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_GRILL_FLIP = register("block.grill.flip");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_DIVING_BOARD_BOUNCE = register("block.diving_board.bounce");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_FRIDGE_OPEN = register("block.fridge.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_FRIDGE_CLOSE = register("block.fridge.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TOILET_FLUSH = register("block.toilet.flush");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_LIGHT_SWITCH = register("block.light_switch.click");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TAP = register("block.tap.run");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_SHOWER = register("block.shower.run");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_SLIDING_DOOR_OPEN = register("block.sliding_door.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_SLIDING_DOOR_CLOSE = register("block.sliding_door.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_MICROWAVE_RUNNING = register("block.microwave.running");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_MICROWAVE_FINISH = register("block.microwave.finish");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TOASTER_DOWN = register("block.toaster.down");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TOASTER_UP = register("block.toaster.up");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BLENDER = register("block.blender.run");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_DISHWASHER = register("block.dishwasher.run");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_WASHING_MACHINE = register("block.washing_machine.run");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_TV_STATIC = register("block.tv.static");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_DOOR_BELL = register("block.door_bell.ring");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_FIRE_ALARM = register("block.fire_alarm.ring");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_ELECTRIC_FENCE_ZAP = register("block.electric_fence.zap");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_CHOPPING_BOARD_CHOP = register("block.chopping_board.chop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BIN_OPEN = register("block.bin.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BIN_CLOSE = register("block.bin.close");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name)
    {
        return REGISTER.register(name, SoundEvent::createVariableRangeEvent);
    }
}
