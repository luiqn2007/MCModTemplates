package com.example.examplemod.block.chest;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Require {@link com.example.examplemod.mixin.chest.MixinSheets}
 */
public class WoodenChest extends ChestBlock {

    private static final List<RegistryObject<WoodenChest>> CHESTS = new LinkedList<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public static RegistryObject<WoodenChest> of(ResourceLocation name, MaterialColor color, CreativeModeTab tab, Supplier<? extends Block> planks, boolean isTrapped,
                                                 DeferredRegister<Block> blocks, DeferredRegister<Item> items, DeferredRegister<BlockEntityType<?>> entities) {
        ChestHolder holder = new ChestHolder();
        holder.isTrapped = isTrapped;
        holder.name = name;
        holder.chest = blocks.register(name.getPath(), () -> new WoodenChest(color, planks, holder));
        holder.entity = entities.register(name.getPath(), () -> new BlockEntityType<>((pPos, pState) -> new WoodenChestEntity(holder, pPos, pState), holder.entityBlocks, null));
        holder.item = items.register(name.getPath(), () -> WoodenChestItem.create(holder.chest.get(), tab));
        CHESTS.add(holder.chest);
        return holder.chest;
    }

    public static RegistryObject<WoodenChest> of(String modid, String name, MaterialColor color, CreativeModeTab tab, Supplier<? extends Block> planks, boolean isTrapped,
                                                 DeferredRegister<Block> blocks, DeferredRegister<Item> items, DeferredRegister<BlockEntityType<?>> entities) {
        name = name.toLowerCase(Locale.ROOT);
        return of(new ResourceLocation(modid, isTrapped ? "trapped_" + name : name), color, tab, planks, isTrapped, blocks, items, entities);
    }

    public static RegistryObject<WoodenChest> of(String name, MaterialColor color, CreativeModeTab tab, Supplier<? extends Block> planks, boolean isTrapped,
                                                 DeferredRegister<Block> blocks, DeferredRegister<Item> items, DeferredRegister<BlockEntityType<?>> entities) {
        ResourceLocation tmp = new ResourceLocation(name);
        return of(tmp.getNamespace(), tmp.getPath(), color, tab, planks, isTrapped, blocks, items, entities);
    }

    public static Stream<RegistryObject<WoodenChest>> stream() {
        return CHESTS.stream();
    }

    public final ResourceLocation name;
    public final boolean isTrapped;
    public final ChestHolder holder;

    public final RegistryObject<BlockEntityType<WoodenChestEntity>> entity;
    public final RegistryObject<WoodenChestItem> item;

    private final Supplier<? extends Block> planks;
    private final ChestRegisterHelper register = new ChestRegisterHelper();
    @Nullable
    private Object renderer = null;

    public WoodenChest(MaterialColor color, Supplier<? extends Block> planks, ChestHolder holder) {
        super(holder.isTrapped ? BlockBehaviour.Properties.copy(Blocks.TRAPPED_CHEST).color(color)
                        : BlockBehaviour.Properties.copy(Blocks.CHEST).color(color), holder.entity::get);
        this.name = holder.name;
        this.planks = planks;
        this.entity = holder.entity;
        this.item = holder.item;
        this.isTrapped = holder.isTrapped;
        this.holder = holder;

        holder.entityBlocks.add(this);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntityType.get().create(pPos, pState);
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        if (isTrapped) {
            return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
        }
        return super.getOpenChestStat();
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return isTrapped || super.isSignalSource(pState);
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        if (isTrapped) {
            return Mth.clamp(ChestBlockEntity.getOpenCount(pLevel, pPos), 0, 15);
        }
        return super.getSignal(pState, pLevel, pPos, pDirection);
    }

    @Override
    public int getDirectSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        if (isTrapped) {
            return pDirection == Direction.UP ? pState.getSignal(pLevel, pPos, pDirection) : 0;
        }
        return super.getDirectSignal(pState, pLevel, pPos, pDirection);
    }

    public BlockEntityType<WoodenChestEntity> entity() {
        return entity.get();
    }

    public WoodenChestItem item() {
        return item.get();
    }

    public Block planks() {
        return planks.get();
    }

    public ChestRegisterHelper register() {
        return register;
    }

    @OnlyIn(Dist.CLIENT)
    public WoodenChestRenderer renderer() {
        if (renderer == null) {
            renderer = new WoodenChestRenderer(this);
        }
        return (WoodenChestRenderer) renderer;
    }

    public class ChestRegisterHelper {

        /**
         * Call when {@link EntityRenderersEvent.RegisterRenderers} event fired
         */
        @OnlyIn(Dist.CLIENT)
        public void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(entity(), renderer()::getBlockRenderer);
        }

        // DataProvider ====================================================================================================

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, add chinese name
         *
         * @param provider language provider
         * @param chinese  tree chinese name
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesZh(LanguageProvider provider, String chinese) {
            provider.add(WoodenChest.this, isTrapped ? chinese + "陷阱箱" : chinese + "木箱子");
        }

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
         *
         * @param provider language provider
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesEn(LanguageProvider provider) {
            String name = Arrays.stream(WoodenChest.this.name.getPath().split("_"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.length() == 1 ? s.toUpperCase(Locale.ROOT) : Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .collect(Collectors.joining(" "));
            addLanguagesEn(provider, name);
        }

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
         *
         * @param provider language provider
         * @param english  english name
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesEn(LanguageProvider provider, String english) {
            if (isTrapped) {
                provider.add(WoodenChest.this, english + " Trapped Chest");
            } else {
                provider.add(WoodenChest.this, english + " Chest");
            }
        }

        /**
         * call this method in {@link BlockStateProvider#registerStatesAndModels} or other equivalent method.
         *
         * @param provider block model provider
         */
        @SuppressWarnings("JavadocReference")
        public void addBlockModels(BlockStateProvider provider) {
            provider.simpleBlock(WoodenChest.this, provider.models()
                    .getBuilder(delegate.name().getPath())
                    .texture("particle", provider.blockTexture(planks())));
        }

        /**
         * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
         *
         * @param provider item model provider
         */
        @SuppressWarnings("JavadocReference")
        public void addItemModels(ItemModelProvider provider) {
            ResourceLocation name = planks().delegate.name();
            ResourceLocation texPlank = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
            provider.singleTexture(delegate.name().getPath(), provider.mcLoc("item/chest"), "particle", texPlank);
        }

        /**
         * call this method in {@link RecipeProvider#buildCraftingRecipes} or other equivalent method.
         *
         * @param consumer save consumer
         * @param log      log block, if not is null, add recipe: log -> planks * 4
         */
        @SuppressWarnings("JavadocReference")
        public void addRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(WoodenChest.this).group("chest")
                    .define('#', planks())
                    .pattern("###")
                    .pattern("# #")
                    .pattern("###")
                    .unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(planks()))
                    .save(consumer);
        }

        /**
         * call this method in {@link RecipeProvider#buildCraftingRecipes}
         * or other equivalent method.
         *
         * @param consumer save consumer
         * @param log      log block, if not is null, add recipe: log -> planks * 4
         */
        @SuppressWarnings("JavadocReference")
        public void addTrappedRecipes(Consumer<FinishedRecipe> consumer, ItemLike normalChest) {
            ShapelessRecipeBuilder.shapeless(WoodenChest.this).group("trapped_chest")
                    .requires(normalChest)
                    .requires(Items.TRIPWIRE_HOOK)
                    .unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(planks()))
                    .save(consumer);
        }

        /**
         * call this method in {@link TagsProvider#addTags} or other equivalent method.
         *
         * @param tag tag provider
         */
        @SuppressWarnings("JavadocReference")
        public void addBlockTags(Function<TagKey<Block>, TagsProvider.TagAppender<Block>> tag) {
            if (isTrapped) {
                tag.apply(Tags.Blocks.CHESTS_TRAPPED).add(WoodenChest.this);
                tag.apply(Tags.Blocks.CHESTS_WOODEN).add(WoodenChest.this);
                tag.apply(BlockTags.GUARDED_BY_PIGLINS).add(WoodenChest.this);
                tag.apply(BlockTags.MINEABLE_WITH_AXE).add(WoodenChest.this);
            } else {
                tag.apply(Tags.Blocks.CHESTS_WOODEN).add(WoodenChest.this);
                tag.apply(BlockTags.GUARDED_BY_PIGLINS).add(WoodenChest.this);
                tag.apply(BlockTags.MINEABLE_WITH_AXE).add(WoodenChest.this);
                tag.apply(BlockTags.FEATURES_CANNOT_REPLACE).add(WoodenChest.this);
            }
        }

        /**
         * call this method in {@link TagsProvider#addTags} or other equivalent method.
         *
         * @param tag tag provider
         */
        @SuppressWarnings("JavadocReference")
        public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
            if (isTrapped) {
                tag.apply(Tags.Items.CHESTS_TRAPPED).add(asItem());
                tag.apply(Tags.Items.CHESTS_WOODEN).add(asItem());
            } else {
                tag.apply(Tags.Items.CHESTS_WOODEN).add(asItem());
            }
        }

        /**
         * call this method in {@link LootTableProvider#getTables()} or other equivalent method.
         *
         * @param consumer consumer to collect table builder
         */
        @SuppressWarnings("JavadocReference")
        public void addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
            consumer.accept(Pair.of(() -> new BlockLoot() {
                @Override
                protected void addTables() {
                    add(WoodenChest.this, createNameableBlockEntityTable(WoodenChest.this));
                }

                @Override
                protected Iterable<Block> getKnownBlocks() {
                    return Set.of(WoodenChest.this);
                }
            }, LootContextParamSets.BLOCK));
        }

        /**
         * Call in somewhere can get ExistingFileHelper,
         * like {@link net.minecraftforge.forge.event.lifecycle.GatherDataEvent} event
         * @param helper file helper
         */
        public void verifyTexture(ExistingFileHelper helper) {
            String prefix = "textures/entity/chest/" + name.getPath();
            ResourceLocation main = new ResourceLocation(name.getNamespace(), prefix + ".png");
            if (!helper.exists(main, PackType.CLIENT_RESOURCES)) {
                LOGGER.warn("Not found chest skin at {}", main);
            }
            ResourceLocation left = new ResourceLocation(name.getNamespace(), prefix + "_left.png");
            if (!helper.exists(left, PackType.CLIENT_RESOURCES)) {
                LOGGER.warn("Not found chest skin at {}", left);
            }
            ResourceLocation right = new ResourceLocation(name.getNamespace(), prefix + "_right.png");
            if (!helper.exists(right, PackType.CLIENT_RESOURCES)) {
                LOGGER.warn("Not found chest skin at {}", right);
            }
        }
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static class ChestHolder {
        public RegistryObject<WoodenChest> chest;
        public RegistryObject<BlockEntityType<WoodenChestEntity>> entity;
        public RegistryObject<WoodenChestItem> item;

        public ResourceLocation name;
        public boolean isTrapped;
        public final Set<Block> entityBlocks = new HashSet<>();
    }
}
