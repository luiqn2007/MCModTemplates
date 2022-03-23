package com.example.examplemod.woodwork.with_at;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * If you use custom block, and need custom drop loot table, you should
 * implement {@link WoodworkBlockLoot.ILootable} interface to the custom block.
 */
public class WoodworkBuilder {

    final ResourceLocation name;
    CreativeModeTab tab = CreativeModeTab.TAB_DECORATIONS;
    MaterialColor plankColor = MaterialColor.WOOD;

    Function<Woodwork, Block> planks = woodwork -> new Block(BlockBehaviour.Properties.of(Material.WOOD, woodwork.plankColor)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> planksItem = defaultBlockItem(Woodwork::planks);

    Function<Woodwork, StandingSignBlock> sign = woodwork ->
            new StandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.plankColor)
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.WOOD), woodwork.type);
    Function<Woodwork, WallSignBlock> wallSign = woodwork ->
            new WallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.plankColor)
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .lootFrom(woodwork.sign), woodwork.type);
    Function<Woodwork, BlockItem> signItem = woodwork ->
            new SignItem(new Item.Properties().stacksTo(16).tab(tab), woodwork.sign(), woodwork.wallSign());

    Function<Woodwork, PressurePlateBlock> pressurePlate = woodwork -> new PressurePlateBlock(
            PressurePlateBlock.Sensitivity.EVERYTHING,
            BlockBehaviour.Properties.of(Material.WOOD, woodwork.planks().defaultMaterialColor())
                    .noCollission()
                    .strength(0.5F)
                    .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> pressurePlateItem = defaultBlockItem(Woodwork::pressurePlate);

    Function<Woodwork, TrapDoorBlock> trapdoor = woodwork ->
            new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.plankColor)
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .isValidSpawn((_1, _2, _3, _4) -> false));
    Function<Woodwork, BlockItem> trapdoorItem = defaultBlockItem(Woodwork::trapdoor);

    Function<Woodwork, StairBlock> stairs = woodwork -> new StairBlock(() -> woodwork.planks().defaultBlockState(),
            BlockBehaviour.Properties.copy(woodwork.planks()));
    Function<Woodwork, BlockItem> stairsItem = defaultBlockItem(Woodwork::stairs);

    Function<Woodwork, ButtonBlock> button = woodwork -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION)
            .noCollission()
            .strength(0.5F)
            .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> buttonItem = defaultBlockItem(Woodwork::button);

    Function<Woodwork, SlabBlock> slab = woodwork -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.plankColor)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> slabItem = defaultBlockItem(Woodwork::slab);

    Function<Woodwork, FenceGateBlock> fenceGate = woodwork ->
            new FenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.planks().defaultMaterialColor())
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> fenceGateItem = defaultBlockItem(Woodwork::fenceGate);

    Function<Woodwork, FenceBlock> fence = woodwork ->
            new FenceBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.planks().defaultMaterialColor())
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD));
    Function<Woodwork, BlockItem> fenceItem = defaultBlockItem(Woodwork::fence);

    Function<Woodwork, DoorBlock> door = woodwork ->
            new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, woodwork.planks().defaultMaterialColor())
                    .strength(3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion());
    Function<Woodwork, BlockItem> doorItem = woodwork ->
            new DoubleHighBlockItem(woodwork.door(), new Item.Properties().tab(woodwork.tab));

    @Nullable
    Function<Woodwork, ChestBlock> chest = woodwork ->
            new ChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST).color(woodwork.plankColor), () -> BlockEntityType.CHEST);
    Function<Woodwork, BlockItem> chestItem = defaultBlockItem(Woodwork::chest);

    Function<Woodwork, BoatItem> boat = woodwork -> {
        BoatItem2 item = new BoatItem2(woodwork, new Item.Properties().stacksTo(1).tab(woodwork.tab));
        DispenserBlock.registerBehavior(item, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
                Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
                Level level = pSource.getLevel();
                double d0 = pSource.x() + direction.getStepX() * 1.125F;
                double d1 = pSource.y() + direction.getStepY() * 1.125F;
                double d2 = pSource.z() + direction.getStepZ() * 1.125F;
                BlockPos blockpos = pSource.getPos().relative(direction);
                double d3;
                if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                    d3 = 1.0D;
                } else {
                    if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
                        return this.defaultDispenseItemBehavior.dispense(pSource, pStack);
                    }

                    d3 = 0.0D;
                }

                Boat2 boat = new Boat2(level, d0, d1 + d3, d2);
                boat.setType(Boat.Type.OAK);
                boat.setWoodwork(woodwork);
                boat.setYRot(direction.toYRot());
                level.addFreshEntity(boat);
                pStack.shrink(1);
                return pStack;
            }
        });
        return item;
    };

    boolean customSignEntity = false;
    boolean customChestEntity = false;

    public WoodworkBuilder(ResourceLocation name) {
        this.name = name;
    }

    public WoodworkBuilder defaultTab(CreativeModeTab tab) {
        this.tab = tab;
        return this;
    }

    public WoodworkBuilder plankColor(MaterialColor color) {
        this.plankColor = color;
        return this;
    }

    public WoodworkBuilder customPlanks(Function<Woodwork, Block> factory) {
        this.planks = factory;
        return this;
    }
    
    public WoodworkBuilder customPlanksItem(Function<Woodwork, BlockItem> factory) {
        this.planksItem = factory;
        return this;
    }

    public WoodworkBuilder customSign(Function<Woodwork, StandingSignBlock> factory) {
        return customSign(true, factory);
    }

    /**
     * Set custom sign block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public WoodworkBuilder customSign(boolean customEntity, Function<Woodwork, StandingSignBlock> factory) {
        this.sign = factory;
        this.customSignEntity = customEntity;
        return this;
    }

    public WoodworkBuilder customWallSign(Function<Woodwork, WallSignBlock> factory) {
        return customWallSign(true, factory);
    }

    /**
     * Set custom sign (on a wall) block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public WoodworkBuilder customWallSign(boolean customEntity, Function<Woodwork, WallSignBlock> factory) {
        this.wallSign = factory;
        this.customSignEntity = customEntity;
        return this;
    }

    public WoodworkBuilder customSignItem(Function<Woodwork, BlockItem> factory) {
        this.signItem = factory;
        return this;
    }

    public WoodworkBuilder customPressurePlate(Function<Woodwork, PressurePlateBlock> factory) {
        this.pressurePlate = factory;
        return this;
    }

    public WoodworkBuilder customPressurePlateItem(Function<Woodwork, BlockItem> factory) {
        this.pressurePlateItem = factory;
        return this;
    }

    public WoodworkBuilder customTrapDoor(Function<Woodwork, TrapDoorBlock> factory) {
        this.trapdoor = factory;
        return this;
    }

    public WoodworkBuilder customTrapDoorItem(Function<Woodwork, BlockItem> factory) {
        this.trapdoorItem = factory;
        return this;
    }

    public WoodworkBuilder customStairs(Function<Woodwork, StairBlock> factory) {
        this.stairs = factory;
        return this;
    }

    public WoodworkBuilder customStairsItem(Function<Woodwork, BlockItem> factory) {
        this.stairsItem = factory;
        return this;
    }

    public WoodworkBuilder customButton(Function<Woodwork, ButtonBlock> factory) {
        this.button = factory;
        return this;
    }

    public WoodworkBuilder customButtonItem(Function<Woodwork, BlockItem> factory) {
        this.buttonItem = factory;
        return this;
    }

    public WoodworkBuilder customSlab(Function<Woodwork, SlabBlock> factory) {
        this.slab = factory;
        return this;
    }

    public WoodworkBuilder customSlabItem(Function<Woodwork, BlockItem> factory) {
        this.slabItem = factory;
        return this;
    }

    public WoodworkBuilder customFenceGate(Function<Woodwork, FenceGateBlock> factory) {
        this.fenceGate = factory;
        return this;
    }

    public WoodworkBuilder customFenceGateItem(Function<Woodwork, BlockItem> factory) {
        this.fenceGateItem = factory;
        return this;
    }

    public WoodworkBuilder customFence(Function<Woodwork, FenceBlock> factory) {
        this.fence = factory;
        return this;
    }

    public WoodworkBuilder customFenceItem(Function<Woodwork, BlockItem> factory) {
        this.fenceItem = factory;
        return this;
    }

    public WoodworkBuilder customDoor(Function<Woodwork, DoorBlock> factory) {
        this.door = factory;
        return this;
    }

    public WoodworkBuilder customDoorItem(Function<Woodwork, BlockItem> factory) {
        this.doorItem = factory;
        return this;
    }

    public WoodworkBuilder noChest() {
        this.chest = null;
        return this;
    }

    public WoodworkBuilder customChest(Function<Woodwork, ChestBlock> factory) {
        return customChest(true, factory);
    }

    /**
     * Set custom chest block factory
     *
     * @param customEntity true if use custom block entity
     * @param factory      factory to create block
     * @return this builder
     */
    public WoodworkBuilder customChest(boolean customEntity, Function<Woodwork, ChestBlock> factory) {
        this.chest = factory;
        this.customChestEntity = customEntity;
        return this;
    }

    public WoodworkBuilder customChestItem(Function<Woodwork, BlockItem> factory) {
        this.chestItem = factory;
        return this;
    }

    private Function<Woodwork, BlockItem> defaultBlockItem(Function<Woodwork, Block> block) {
        return woodwork -> new BlockItem(block.apply(woodwork), new Item.Properties().tab(woodwork.tab));
    }

    public Woodwork build(DeferredRegister<Block> blocks, DeferredRegister<Item> items,
                          @Nullable DeferredRegister<EntityType<?>> entities) {
        return new Woodwork(this, blocks, items, entities);
    }
}
