package com.mrcrayfish.furniture.core;

import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.block.*;
import com.mrcrayfish.furniture.tileentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CabinetBlockEntity>> CABINET = register("cabinet", CabinetBlockEntity::new, CabinetBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BedsideCabinetBlockEntity>> BEDSIDE_CABINET = register("bedside_cabinet", BedsideCabinetBlockEntity::new, BedsideCabinetBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeskCabinetBlockEntity>> DESK_CABINET = register("desk_cabinet", DeskCabinetBlockEntity::new, DeskCabinetBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrateBlockEntity>> CRATE = register("crate", CrateBlockEntity::new, CrateBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MailBoxBlockEntity>> MAIL_BOX = register("mail_box", MailBoxBlockEntity::new, MailBoxBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TrampolineBlockEntity>> TRAMPOLINE = register("trampoline", TrampolineBlockEntity::new, TrampolineBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoolerBlockEntity>> COOLER = register("cooler", CoolerBlockEntity::new, CoolerBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrillBlockEntity>> GRILL = register("grill", GrillBlockEntity::new, GrillBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DoorMatBlockEntity>> DOOR_MAT = register("door_mat", DoorMatBlockEntity::new, DoorMatBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KitchenDrawerBlockEntity>> KITCHEN_DRAWER = register("kitchen_drawer", KitchenDrawerBlockEntity::new, KitchenDrawerBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KitchenSinkBlockEntity>> KITCHEN_SINK = register("kitchen_sink", KitchenSinkBlockEntity::new, KitchenSinkBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FridgeBlockEntity>> FRIDGE = register("fridge", FridgeBlockEntity::new, FridgeBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FreezerBlockEntity>> FREEZER = register("freezer", FreezerBlockEntity::new, FreezerBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LightSwitchBlockEntity>> LIGHT_SWITCH = register("light_switch", LightSwitchBlockEntity::new, LightSwitchBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShowerHeadBlockEntity>> SHOWER_HEAD = register("shower_head", ShowerHeadBlockEntity::new, ShowerHeadBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OvenBlockEntity>> OVEN = register("oven", OvenBlockEntity::new, OvenBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MicrowaveBlockEntity>> MICROWAVE = register("microwave", MicrowaveBlockEntity::new, MicrowaveBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ToasterBlockEntity>> TOASTER = register("toaster", ToasterBlockEntity::new, ToasterBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlenderBlockEntity>> BLENDER = register("blender", BlenderBlockEntity::new, BlenderBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DishwasherBlockEntity>> DISHWASHER = register("dishwasher", DishwasherBlockEntity::new, DishwasherBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WashingMachineBlockEntity>> WASHING_MACHINE = register("washing_machine", WashingMachineBlockEntity::new, WashingMachineBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CupBlockEntity>> CUP = register("cup", CupBlockEntity::new, CupBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TVBlockEntity>> TV = register("tv", TVBlockEntity::new, TVBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TVStandBlockEntity>> TV_STAND = register("tv_stand", TVStandBlockEntity::new, TVStandBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StereoBlockEntity>> STEREO = register("stereo", StereoBlockEntity::new, StereoBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ComputerBlockEntity>> COMPUTER = register("computer", ComputerBlockEntity::new, ComputerBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrinterBlockEntity>> PRINTER = register("printer", PrinterBlockEntity::new, PrinterBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DigitalClockBlockEntity>> DIGITAL_CLOCK = register("digital_clock", DigitalClockBlockEntity::new, DigitalClockBlock.class);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PhotoFrameBlockEntity>> PHOTO_FRAME = register("photo_frame", PhotoFrameBlockEntity::new, PhotoFrameBlock.class);

    /**
     * Registers a block entity type that is valid for every furniture block of the given class (every wood and colour of it). Blocks
     * are registered before block entity types, so they are all known by then.
     */
    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Class<? extends Block> blockClass)
    {
        return REGISTER.register(name, () -> new BlockEntityType<>(supplier, blocksOf(blockClass)));
    }

    private static Set<Block> blocksOf(Class<? extends Block> blockClass)
    {
        Set<Block> blocks = ModBlocks.REGISTER.getEntries().stream().map(DeferredHolder::get).filter(blockClass::isInstance).collect(Collectors.toUnmodifiableSet());
        if(blocks.isEmpty())
        {
            throw new IllegalStateException("No blocks of " + blockClass.getSimpleName());
        }
        return blocks;
    }
}
