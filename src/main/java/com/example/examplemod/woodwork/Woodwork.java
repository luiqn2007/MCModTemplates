package com.example.examplemod.woodwork;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
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
 *
 * <p>It need mixin {@link com.example.examplemod.mixin.woodwork.MixinSheets} to initialize chest texture
 * if the woodwork contains chest or trapped chest</p>
 *
 * <p>Require textures:</p>
 * <ul>
 *     <li>Item: boat, door, sign</li>
 *     <li>Block: planks</li>
 *     <li>Door: assets.[modid].textures.block.[woodwork]_door_top.png</li>
 *     <li>Door: assets.[modid].textures.block.[woodwork]_door_botton.png</li>
 *     <li>Boat: assets.[modid].textures.entity.boat.[woodwork].png</li>
 *     <li>Sign: assets.[modid].textures.entity.signs.[woodwork].png</li>
 *     <li>Chest(normal):  assets.[modid].textures.entity.chest.[woodwork].png</li>
 *     <li>Chest(left):  assets.[modid].textures.entity.chest.[woodwork]_left.png</li>
 *     <li>Chest(right):  assets.[modid].textures.entity.chest.[woodwork]_right.png</li>
 *     <li>TrappedChest(normal):  assets.[modid].textures.entity.chest.[woodwork]_trapped.png</li>
 *     <li>TrappedChest(left):  assets.[modid].textures.entity.chest.[woodwork]_trapped_left.png</li>
 *     <li>TrappedChest(right):  assets.[modid].textures.entity.chest.[woodwork]_trapped_right.png</li>
 * </ul>
 *
 * <p>Two door textures is optional. If not exist, use planks texture.</p>
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

        this.planks = addBlock(builder.planks);
        this.sign = addBlock(builder.sign, WoodworkManager.signBlocks);
        this.wallSign = addBlock(builder.wallSign, WoodworkManager.signBlocks);
        this.pressurePlate = addBlock(builder.pressurePlate);
        this.trapdoor = addBlock(builder.trapdoor);
        this.stairs = addBlock(builder.stairs);
        this.button = addBlock(builder.button);
        this.slab = addBlock(builder.slab);
        this.fenceGate = addBlock(builder.fenceGate);
        this.fence = addBlock(builder.fence);
        this.door = addBlock(builder.door);
        this.chest = builder.chest.noBlock ? null : addBlock(builder.chest, WoodworkManager.chestBlocks);
        this.trappedChest = builder.trappedChest.noBlock ? null : addBlock(builder.trappedChest, WoodworkManager.trappedChestBlocks);
        this.boat = register(WoodworkManager.items(), "boat", () -> {
            BoatItem item = builder.boat.apply(builder.boatProperties.apply(this), this);
            allItems.add(item);
            return item;
        });
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

    private <B extends Block> RegistryObject<B> addBlock(BlockFactory<B, ? extends BlockItem> factory, @Nullable Set<Block> entityBlocks) {
        RegistryObject<B> block = register(WoodworkManager.blocks(), factory.name, () -> {
            B b = factory.newBlock(this);
            allBlocks.add(b);
            if (entityBlocks != null && !factory.customEntity) {
                entityBlocks.add(b);
            }
            return b;
        });
        register(WoodworkManager.items(), block, () -> {
            BlockItem i = factory.newItem(this);
            allItems.add(i);
            return i;
        });
        return block;
    }

    private <B extends Block> RegistryObject<B> addBlock(BlockFactory<B, ? extends BlockItem> factory) {
        return addBlock(factory, null);
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
