package com.example.examplemod.woodwork.with_at;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Woodwork, from planks to sign, trapdoor, button, fence, door, and other all vanilla wooden blocks and item(boat),
 * based on at
 *
 * <p>In this version, your boat layer texture is in resources/assets/minecraft/ </p>
 *
 * <li>{@code public-f net.minecraft.world.level.block.entity.BlockEntityType f_58915_ # validBlocks}</li>
 * <li>{@code public net.minecraft.data.recipes.RecipeProvider m_176580_(Ljava/util/function/Consumer;Lnet/minecraft/data/BlockFamily;)V # generateRecipes}</li>
 */
public class Woodwork {

    private static final HashMap<ResourceLocation, Woodwork> WOODWORK_NAME_MAP = new HashMap<>();
    private static final HashMap<WoodType, Woodwork> WOODWORK_TYPE_MAP = new HashMap<>();
    @Nullable
    private static RegistryObject<EntityType<Boat2>> BOAT_ENTITY_TYPE = null;

    public static Optional<Woodwork> getWoodwork(ResourceLocation name) {
        return Optional.ofNullable(WOODWORK_NAME_MAP.get(name));
    }

    public static Optional<Woodwork> getWoodwork(String name) {
        return getWoodwork(new ResourceLocation(name));
    }

    public static Optional<Woodwork> getWoodwork(WoodType type) {
        return Optional.ofNullable(WOODWORK_TYPE_MAP.get(type));
    }

    public static EntityType<Boat2> boatEntityType() {
        return Objects.requireNonNull(BOAT_ENTITY_TYPE).get();
    }

    public static WoodworkBuilder builder(ResourceLocation name) {
        return new WoodworkBuilder(name);
    }

    public static WoodworkBuilder builder(String modid, String name) {
        return new WoodworkBuilder(new ResourceLocation(modid, name));
    }

    // properties
    public final MaterialColor plankColor;
    public final WoodType type;
    public final ResourceLocation name;
    public final CreativeModeTab tab;
    private final boolean customSignEntity;
    private final boolean customChestEntity;

    // blocks
    public final RegistryObject<Block> planks;
    public final RegistryObject<StandingSignBlock> sign;
    public final RegistryObject<WallSignBlock> wallSign;
    public final RegistryObject<PressurePlateBlock> pressurePlate;
    public final RegistryObject<TrapDoorBlock> trapdoor;
    public final RegistryObject<StairBlock> stairs;
    public final RegistryObject<ButtonBlock> button;
    public final RegistryObject<SlabBlock> slab;
    public final RegistryObject<FenceGateBlock> fenceGate;
    public final RegistryObject<FenceBlock> fence;
    public final RegistryObject<DoorBlock> door;
    @Nullable
    public final RegistryObject<ChestBlock> chest;
    private final Set<Block> allBlocks = new HashSet<>();

    // items
    public final RegistryObject<BoatItem> boat;
    private final Set<Item> allItems = new HashSet<>();
    private final WoodworkRegister register = new WoodworkRegister(this);

    Object boatLayer = null;

    Woodwork(WoodworkBuilder builder, DeferredRegister<Block> blocks, DeferredRegister<Item> items,
             @Nullable DeferredRegister<EntityType<?>> entities) {
        this.plankColor = builder.plankColor;
        this.name = builder.name;
        this.type = WoodType.register(WoodType.create(name.toString()));
        this.tab = builder.tab;
        this.customChestEntity = builder.customChestEntity;
        this.customSignEntity = builder.customSignEntity;

        this.planks = register(blocks, "planks", asSupplier(builder.planks, allBlocks));
        this.sign = register(blocks, "sign", asSupplier(builder.sign, allBlocks));
        this.wallSign = register(blocks, "wall_sign", asSupplier(builder.wallSign, allBlocks));
        this.pressurePlate = register(blocks, "pressure_plate", asSupplier(builder.pressurePlate, allBlocks));
        this.trapdoor = register(blocks, "trapdoor", asSupplier(builder.trapdoor, allBlocks));
        this.stairs = register(blocks, "stairs", asSupplier(builder.stairs, allBlocks));
        this.button = register(blocks, "button", asSupplier(builder.button, allBlocks));
        this.slab = register(blocks, "slab", asSupplier(builder.slab, allBlocks));
        this.fenceGate = register(blocks, "fence_gate", asSupplier(builder.fenceGate, allBlocks));
        this.fence = register(blocks, "fence", asSupplier(builder.fence, allBlocks));
        this.door = register(blocks, "door", asSupplier(builder.door, allBlocks));
        this.chest = builder.chest == null ? null : register(blocks, "chest", asSupplier(builder.chest, allBlocks));

        register(items, planks, asSupplier(builder.planksItem, allItems));
        register(items, sign, asSupplier(builder.signItem, allItems));
        register(items, pressurePlate, asSupplier(builder.pressurePlateItem, allItems));
        register(items, trapdoor, asSupplier(builder.trapdoorItem, allItems));
        register(items, stairs, asSupplier(builder.stairsItem, allItems));
        register(items, button, asSupplier(builder.buttonItem, allItems));
        register(items, slab, asSupplier(builder.slabItem, allItems));
        register(items, fenceGate, asSupplier(builder.fenceGateItem, allItems));
        register(items, fence, asSupplier(builder.fenceItem, allItems));
        register(items, door, asSupplier(builder.doorItem, allItems));
        if (chest != null) {
            register(items, chest, asSupplier(builder.chestItem, allItems));
        }
        boat = register(items, "boat", asSupplier(builder.boat, allItems));

        if (BOAT_ENTITY_TYPE == null && entities != null) {
            BOAT_ENTITY_TYPE = entities.register("boat2", () -> EntityType.Builder
                    .<Boat2>of(Boat2::new, MobCategory.MISC)
                    .sized(1.375F, 0.5625F)
                    .clientTrackingRange(10).build("boat2"));
        }

        WOODWORK_NAME_MAP.put(name, this);
        WOODWORK_TYPE_MAP.put(type, this);
    }

    public ResourceLocation name() {
        return name;
    }

    public Block planks() {
        return planks.get();
    }

    public StandingSignBlock sign() {
        return sign.get();
    }

    public WallSignBlock wallSign() {
        return wallSign.get();
    }

    public PressurePlateBlock pressurePlate() {
        return pressurePlate.get();
    }

    public TrapDoorBlock trapdoor() {
        return trapdoor.get();
    }

    public StairBlock stairs() {
        return stairs.get();
    }

    public ButtonBlock button() {
        return button.get();
    }

    public SlabBlock slab() {
        return slab.get();
    }

    public FenceGateBlock fenceGate() {
        return fenceGate.get();
    }

    public FenceBlock fence() {
        return fence.get();
    }

    public DoorBlock door() {
        return door.get();
    }

    public ChestBlock chest() {
        return Objects.requireNonNull(chest).get();
    }

    public BoatItem boat() {
        return boat.get();
    }

    public boolean hasChest() {
        return chest != null;
    }

    public boolean hasCustomChestEntity() {
        return customChestEntity;
    }

    public boolean hasCustomSignEntity() {
        return customSignEntity;
    }

    @OnlyIn(Dist.CLIENT)
    public net.minecraft.client.model.geom.ModelLayerLocation boatLayer() {
        return Objects.requireNonNull((net.minecraft.client.model.geom.ModelLayerLocation) boatLayer);
    }

    public Set<Block> allBlocks() {
        return Set.copyOf(allBlocks);
    }

    public Set<Item> allItems() {
        return Set.copyOf(allItems);
    }

    public WoodworkRegister register() {
        return register;
    }

    private <T> Supplier<T> asSupplier(Function<Woodwork, T> factory, Set<? super T> collector) {
        return () -> {
            T element = factory.apply(this);
            collector.add(element);
            return element;
        };
    }

    private <T extends IForgeRegistryEntry<? super T>> void register(DeferredRegister<? super T> register,
                                                                     RegistryObject<?> name, Supplier<T> supplier) {
        register.register(name.getId().getPath(), supplier);
    }

    private <T extends IForgeRegistryEntry<? super T>> RegistryObject<T> register(DeferredRegister<? super T> register,
                                                                                  String postfix, Supplier<T> supplier) {
        return register.register(name.getPath() + "_" + postfix, supplier);
    }
}
