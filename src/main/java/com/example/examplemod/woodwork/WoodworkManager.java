package com.example.examplemod.woodwork;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class WoodworkManager {

    public static WoodworkBuilder builder(ResourceLocation name) {
        return Woodwork.builder(name);
    }

    public static WoodworkBuilder builder(String modid, String name) {
        return Woodwork.builder(modid, name);
    }

    static final HashMap<ResourceLocation, Woodwork> byName = new HashMap<>();
    static final HashMap<WoodType, Woodwork> byType = new HashMap<>();
    static final Set<Block> signBlocks = new HashSet<>();
    static final Set<Block> chestBlocks = new HashSet<>();
    static final Set<Block> trappedChestBlocks = new HashSet<>();

    static boolean isInitialized = false;
    @Nullable
    private static DeferredRegister<Item> items = null;
    @Nullable
    private static DeferredRegister<Block> blocks = null;

    @Nullable
    private static RegistryObject<EntityType<ModBoat>> boatEntityType = null;
    @Nullable
    private static RegistryObject<BlockEntityType<ModSignBlockEntity>> signBlockEntityType = null;
    @Nullable
    private static RegistryObject<BlockEntityType<ModChestBlockEntity>> chestBlockEntityType = null;
    @Nullable
    private static RegistryObject<BlockEntityType<ModChestTrappedBlockEntity>> trappedChestBlockEntityType = null;
    @Nullable
    private static NetworkHolder network = null;

    public static void ensureInitialized() {
        if (!isInitialized) {
            throw new RuntimeException("Please call WoodworkRegister.initialize first.");
        }
    }

    public static boolean initialize(DeferredRegister<Item> items, DeferredRegister<Block> blocks,
                                     DeferredRegister<BlockEntityType<?>> blockEntities,
                                     DeferredRegister<EntityType<?>> entities, NetworkHolder network) {
        if (isInitialized) {
            return true;
        }

        WoodworkManager.items = items;
        WoodworkManager.blocks = blocks;
        WoodworkManager.network = network;

        WoodworkManager.boatEntityType = entities.register("boat2", () -> EntityType.Builder
                .<ModBoat>of(ModBoat::new, MobCategory.MISC)
                .sized(1.375F, 0.5625F)
                .clientTrackingRange(10).build("boat2"));

        WoodworkManager.signBlockEntityType = blockEntities.register("mod_sign_entity",
                () -> new BlockEntityType<>(ModSignBlockEntity::new, signBlocks, null));
        network.register(ModSignEditOpenPkt.class, ModSignEditOpenPkt::write, ModSignEditOpenPkt::read, ModSignEditOpenPkt::handleClient);
        network.register(ModSignTextUpdatePkt.class, ModSignTextUpdatePkt::write, ModSignTextUpdatePkt::read, ModSignTextUpdatePkt::handleServer);

        WoodworkManager.chestBlockEntityType = blockEntities.register("mod_chest_entity",
                () -> new BlockEntityType<>(ModChestBlockEntity::new, chestBlocks, null));

        WoodworkManager.trappedChestBlockEntityType = blockEntities.register("mod_trapped_chest_entity",
                () -> new BlockEntityType<>(ModChestTrappedBlockEntity::new, trappedChestBlocks, null));
        isInitialized = true;

        return true;
    }

    public static Optional<Woodwork> get(ResourceLocation name) {
        return Optional.ofNullable(byName.get(name));
    }

    public static Woodwork getOrThrow(ResourceLocation name) {
        return get(name).orElseThrow();
    }

    public static Optional<Woodwork> get(WoodType type) {
        return Optional.ofNullable(byType.get(type));
    }

    public static Woodwork getOrThrow(WoodType type) {
        return get(type).orElseThrow();
    }

    public static Set<ResourceLocation> allNames() {
        return Set.copyOf(byName.keySet());
    }

    public static Woodwork register(WoodworkBuilder builder) {
        Woodwork woodwork = builder.build();
        byName.put(woodwork.name, woodwork);
        byType.put(woodwork.type, woodwork);
        return woodwork;
    }

    public static DeferredRegister<Item> items() {
        ensureInitialized();
        return items;
    }

    public static DeferredRegister<Block> blocks() {
        ensureInitialized();
        return blocks;
    }

    public static NetworkHolder network() {
        ensureInitialized();
        return network;
    }

    public static EntityType<ModBoat> boatEntityType() {
        ensureInitialized();
        return boatEntityType.get();
    }

    public static BlockEntityType<ModSignBlockEntity> signBlockEntityType() {
        ensureInitialized();
        return signBlockEntityType.get();
    }

    public static BlockEntityType<ModChestBlockEntity> chestBlockEntityType() {
        ensureInitialized();
        return chestBlockEntityType.get();
    }

    public static BlockEntityType<ModChestTrappedBlockEntity> trappedChestBlockEntityType() {
        ensureInitialized();
        return trappedChestBlockEntityType.get();
    }
}
