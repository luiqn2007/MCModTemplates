package com.example.examplemod.woodwork;

import net.minecraft.resources.ResourceLocation;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Woodwork, from planks to sign, trapdoor, button, fence, door, and other all vanilla wooden blocks and item(boat),
 *
 * <p>Require textures:</p>
 * <ul>
 *     <li>Item and block textures</li>
 *     <li>Boat: assets.[modid].textures.entity.boat.[woodwork].png</li>
 *     <li>Sign: assets.[modid].textures.entity.signs.[woodwork].png</li>
 *     <li>Chest(normal):  assets.[modid].textures.entity.chest.[woodwork].png</li>
 *     <li>Chest(left):  assets.[modid].textures.entity.chest.[woodwork]_left.png</li>
 *     <li>Chest(right):  assets.[modid].textures.entity.chest.[woodwork]_right.png</li>
 *     <li>TrappedChest(normal):  assets.[modid].textures.entity.chest.[woodwork]_trapped.png</li>
 *     <li>TrappedChest(left):  assets.[modid].textures.entity.chest.[woodwork]_trapped_left.png</li>
 *     <li>TrappedChest(right):  assets.[modid].textures.entity.chest.[woodwork]_trapped_right.png</li>
 * </ul>
 */
public class Woodwork {

    public static WoodworkBuilder builder(ResourceLocation name) {
        WoodworkManager.ensureInitialized();
        return new WoodworkBuilder(name);
    }

    public static WoodworkBuilder builder(String modid, String name) {
        WoodworkManager.ensureInitialized();
        return new WoodworkBuilder(new ResourceLocation(modid, name));
    }

    // properties
    public final MaterialColor plankColor;
    public final WoodType type;
    public final ResourceLocation name;
    public final CreativeModeTab tab;

    // blocks
    public final RegistryObject<Block> planks;
    public final RegistryObject<SignBlock> sign;
    public final RegistryObject<SignBlock> wallSign;
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
    @Nullable
    public final RegistryObject<ChestBlock> trappedChest;
    private final Set<Block> allBlocks = new HashSet<>();

    // items
    public final RegistryObject<BoatItem> boat;
    private final Set<Item> allItems = new HashSet<>();
    private final WoodworkRegisterHelper register = new WoodworkRegisterHelper(this);

    @Nullable
    Object boatLayer = null;

    Woodwork(WoodworkBuilder builder) {
        this.plankColor = builder.plankColor;
        this.name = builder.name;
        this.type = WoodType.register(WoodType.create(name.toString()));
        this.tab = builder.tab;

        this.planks = register(WoodworkManager.blocks(), "planks", asSupplier(builder.planks, allBlocks));
        this.sign = register(WoodworkManager.blocks(), "sign", asSupplier(builder.sign, allBlocks, WoodworkManager.signBlocks, !builder.customSignEntity));
        this.wallSign = register(WoodworkManager.blocks(), "wall_sign", asSupplier(builder.wallSign, allBlocks, WoodworkManager.signBlocks, !builder.customSignEntity));
        this.pressurePlate = register(WoodworkManager.blocks(), "pressure_plate", asSupplier(builder.pressurePlate, allBlocks));
        this.trapdoor = register(WoodworkManager.blocks(), "trapdoor", asSupplier(builder.trapdoor, allBlocks));
        this.stairs = register(WoodworkManager.blocks(), "stairs", asSupplier(builder.stairs, allBlocks));
        this.button = register(WoodworkManager.blocks(), "button", asSupplier(builder.button, allBlocks));
        this.slab = register(WoodworkManager.blocks(), "slab", asSupplier(builder.slab, allBlocks));
        this.fenceGate = register(WoodworkManager.blocks(), "fence_gate", asSupplier(builder.fenceGate, allBlocks));
        this.fence = register(WoodworkManager.blocks(), "fence", asSupplier(builder.fence, allBlocks));
        this.door = register(WoodworkManager.blocks(), "door", asSupplier(builder.door, allBlocks));
        this.chest = builder.chest == null ? null : register(WoodworkManager.blocks(), "chest", asSupplier(builder.chest, allBlocks, WoodworkManager.chestBlocks, !builder.customChestEntity));
        this.trappedChest = builder.trappedChest == null ? null : register(WoodworkManager.blocks(), "trapped_chest", asSupplier(builder.trappedChest, allBlocks, WoodworkManager.trappedChestBlocks, !builder.customTrappedChestEntity));

        register(WoodworkManager.items(), planks, asSupplier(builder.planksItem, allItems));
        register(WoodworkManager.items(), sign, asSupplier(builder.signItem, allItems));
        register(WoodworkManager.items(), pressurePlate, asSupplier(builder.pressurePlateItem, allItems));
        register(WoodworkManager.items(), trapdoor, asSupplier(builder.trapdoorItem, allItems));
        register(WoodworkManager.items(), stairs, asSupplier(builder.stairsItem, allItems));
        register(WoodworkManager.items(), button, asSupplier(builder.buttonItem, allItems));
        register(WoodworkManager.items(), slab, asSupplier(builder.slabItem, allItems));
        register(WoodworkManager.items(), fenceGate, asSupplier(builder.fenceGateItem, allItems));
        register(WoodworkManager.items(), fence, asSupplier(builder.fenceItem, allItems));
        register(WoodworkManager.items(), door, asSupplier(builder.doorItem, allItems));
        if (chest != null) register(WoodworkManager.items(), chest, asSupplier(builder.chestItem, allItems));
        if (trappedChest != null)
            register(WoodworkManager.items(), trappedChest, asSupplier(builder.trappedChestItem, allItems));
        boat = register(WoodworkManager.items(), "boat", asSupplier(builder.boat, allItems));
    }

    public ResourceLocation name() {
        return name;
    }

    public Block planks() {
        return planks.get();
    }

    public SignBlock sign() {
        return sign.get();
    }

    public SignBlock wallSign() {
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

    public ChestBlock trappedChest() {
        return Objects.requireNonNull(trappedChest).get();
    }

    public BoatItem boat() {
        return boat.get();
    }

    public boolean hasChest() {
        return chest != null;
    }

    public boolean hasTrappedChest() {
        return trappedChest != null;
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

    public WoodworkRegisterHelper register() {
        return register;
    }

    private <T> Supplier<T> asSupplier(Function<Woodwork, T> factory, Set<? super T> collector, @Nullable Collection<? super T> list) {
        return () -> {
            T element = factory.apply(this);
            collector.add(element);
            if (list != null) {
                list.add(element);
            }
            return element;
        };
    }

    private <T> Supplier<T> asSupplier(Function<Woodwork, T> factory, Set<? super T> collector, @Nullable Collection<? super T> list, boolean check) {
        return asSupplier(factory, collector, check ? list : null);
    }

    private <T> Supplier<T> asSupplier(Function<Woodwork, T> factory, Set<? super T> collector) {
        return asSupplier(factory, collector, null);
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
