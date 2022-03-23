package com.example.examplemod.woodwork.with_at;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record WoodworkRegister(Woodwork woodwork) {

    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean IS_BOAT_RENDERER_REGISTER = false;

    public void register(IEventBus bus) {
        WoodworkRegisterEvents events = new WoodworkRegisterEvents(this);
        bus.addListener(events::onSetup);
        bus.addListener(events::onClient);
        bus.register(events);
        if (Environment.get().getDist().isClient()) {
            bus.register(events.client());
        }
    }

    /**
     * modifies sign block entity, call after {@link RegistryEvent} event
     */
    public void registerTileEntityModifiers() {
        if (!woodwork.hasCustomSignEntity()) {
            addBlockToEntity(BlockEntityType.SIGN, woodwork.sign(), woodwork.wallSign());
        }
        if (woodwork.hasChest() && !woodwork.hasCustomChestEntity()) {
            addBlockToEntity(BlockEntityType.CHEST, woodwork.chest());
        }
    }

    private void addBlockToEntity(BlockEntityType<?> type, Block... blocks) {
        ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
        builder.addAll(type.validBlocks);
        builder.add(blocks);
        type.validBlocks = builder.build();
    }

    /**
     * Register all block's render types, call in {@link FMLClientSetupEvent}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRender() {
        net.minecraft.client.renderer.RenderType cutout = net.minecraft.client.renderer.RenderType.cutout();
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(woodwork.trapdoor(), cutout);
        net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(woodwork.door(), cutout);
    }

    /**
     * Register boat's layer, call in {@link EntityRenderersEvent.RegisterLayerDefinitions}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerBoatLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ResourceLocation name = woodwork.name();
        ResourceLocation location = new ResourceLocation(name.getNamespace(), "boat/" + name.getPath());
        woodwork.boatLayer = new net.minecraft.client.model.geom.ModelLayerLocation(location, "main");
        event.registerLayerDefinition(woodwork.boatLayer(), net.minecraft.client.model.BoatModel::createBodyModel);
    }

    /**
     * Register boat's renderer, call in {@link EntityRenderersEvent.RegisterRenderers}
     */
    @OnlyIn(Dist.CLIENT)
    public void registerBoatRenderer(EntityRenderersEvent.RegisterRenderers event) {
        if (!IS_BOAT_RENDERER_REGISTER) {
            event.registerEntityRenderer(Woodwork.boatEntityType(), BoatRenderer2::new);
            IS_BOAT_RENDERER_REGISTER = true;
        }
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
        provider.addBlock(woodwork.planks, chinese + "木板");
        provider.addBlock(woodwork.sign, chinese + "木告示牌");
        provider.add(Util.makeDescriptionId("block", woodwork.wallSign.getId()), "墙上的" + chinese + "木告示牌");
        provider.addBlock(woodwork.pressurePlate, chinese + "木压力板");
        provider.addBlock(woodwork.trapdoor, chinese + "木活板门");
        provider.addBlock(woodwork.stairs, chinese + "木楼梯");
        provider.addBlock(woodwork.button, chinese + "木按钮");
        provider.addBlock(woodwork.slab, chinese + "木台阶");
        provider.addBlock(woodwork.fenceGate, chinese + "木栅栏门");
        provider.addBlock(woodwork.fence, chinese + "木栅栏");
        provider.addBlock(woodwork.door, chinese + "木门");
        provider.addItem(woodwork.boat, chinese + "木船");
        if (woodwork.chest != null) {
            provider.addBlock(woodwork.chest, chinese + "木箱子");
        }
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     */
    @SuppressWarnings("JavadocReference")
    public void addLanguagesEn(LanguageProvider provider) {
        addLanguagesEn(provider, defaultEnglishName());
    }

    /**
     * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
     *
     * @param provider language provider
     * @param english  english name
     */
    @SuppressWarnings("JavadocReference")
    public void addLanguagesEn(LanguageProvider provider, String english) {
        provider.addBlock(woodwork.planks, english + " Planks");
        provider.addBlock(woodwork.sign, english + " Sign");
        provider.add(Util.makeDescriptionId("block", woodwork.wallSign.getId()), english + " Wall Sign");
        provider.addBlock(woodwork.pressurePlate, english + " Pressure Plate");
        provider.addBlock(woodwork.trapdoor, english + " Trap Trapdoor");
        provider.addBlock(woodwork.stairs, english + " Stairs");
        provider.addBlock(woodwork.button, english + " Button");
        provider.addBlock(woodwork.slab, english + " Slab");
        provider.addBlock(woodwork.fenceGate, english + " Fence Gate");
        provider.addBlock(woodwork.fence, english + " Fence");
        provider.addBlock(woodwork.door, english + " Door");
        provider.addItem(woodwork.boat, english + " Boat");
        if (woodwork.chest != null) {
            provider.addBlock(woodwork.chest, english + " Chest");
        }
    }

    private String defaultEnglishName() {
        StringBuilder sb = new StringBuilder();
        for (String s : woodwork.name.getPath().split("_")) {
            if (s.isEmpty()) continue;
            String s1 = s.toLowerCase(Locale.ROOT);
            sb.append(Character.toUpperCase(s1.charAt(0)));
            sb.append(s1.substring(1));
        }
        return sb.toString();
    }

    /**
     * call this method in {@link BlockStateProvider#registerStatesAndModels} or other equivalent method.
     *
     * @param provider block model provider
     */
    @SuppressWarnings("JavadocReference")
    public void addBlockModels(BlockStateProvider provider) {
        ExistingFileHelper helper = provider.models().existingFileHelper;
        ExistingFileHelper.ResourceType texType =
                new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

        ResourceLocation texPlank = provider.blockTexture(woodwork.planks());

        provider.simpleBlock(woodwork.planks());
        provider.signBlock(woodwork.sign(), woodwork.wallSign(), texPlank);
        provider.pressurePlateBlock(woodwork.pressurePlate(), texPlank);
        // orientable?
        provider.trapdoorBlock(woodwork.trapdoor(), texPlank, true);
        provider.stairsBlock(woodwork.stairs(), texPlank);
        provider.buttonBlock(woodwork.button(), texPlank);
        provider.slabBlock(woodwork.slab(), texPlank, texPlank);
        provider.fenceGateBlock(woodwork.fenceGate(), texPlank);
        provider.fenceBlock(woodwork.fence(), texPlank);

        DoorBlock door = woodwork.door();
        ResourceLocation doorName = woodwork.door.getId();
        ResourceLocation doorTop = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_top");
        ResourceLocation doorBottom = new ResourceLocation(doorName.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + doorName.getPath() + "_bottom");
        if (!helper.exists(doorTop, texType)) {
            LOGGER.warn(doorTop + " not exist, use " + texPlank);
            doorTop = texPlank;
        }
        if (!helper.exists(doorBottom, texType)) {
            LOGGER.warn(doorBottom + " not exist, use " + texPlank);
            doorBottom = texPlank;
        }
        provider.doorBlock(door, doorBottom, doorTop);

        provider.models().fenceInventory(woodwork.fence.getId().getPath() + "_inventory", texPlank);

        LOGGER.warn("Chest block model not generated, please create it yourself");
    }

    /**
     * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
     *
     * @param provider item model provider
     */
    @SuppressWarnings("JavadocReference")
    public void addItemModels(ItemModelProvider provider) {
        addBlockItem(woodwork.planks, provider);
        addBlockItem(woodwork.slab, provider);
        addBlockItem(woodwork.fence, "inventory", provider);
        addBlockItem(woodwork.stairs, provider);
        addBlockItem(woodwork.button, provider);
        addBlockItem(woodwork.pressurePlate, provider);
        addBlockItem(woodwork.trapdoor, "bottom", provider);
        addBlockItem(woodwork.fenceGate, provider);

        addItem(woodwork.boat, "generated", provider);
        addItem(woodwork.door, "generated", provider);
        addItem(woodwork.sign, "generated", provider);

        LOGGER.warn("Chest item model not generated, please create it yourself");
    }

    private void addBlockItem(RegistryObject<? extends Block> block, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path));
    }

    private void addBlockItem(RegistryObject<? extends Block> block, String postfix, ItemModelProvider provider) {
        String path = block.getId().getPath();
        provider.withExistingParent(path, provider.modLoc("block/" + path + "_" + postfix));
    }

    private void addItem(RegistryObject<? extends ItemLike> item, String type, ItemModelProvider provider) {
        ResourceLocation name = item.get().asItem().delegate.name();
        provider.singleTexture(name.getPath(), provider.mcLoc("item/" + type),
                "layer0", provider.modLoc("item/" + name.getPath()));
    }

    /**
     * call this method in {@link RecipeProvider#buildCraftingRecipes}
     * or other equivalent method.
     *
     * @param consumer save consumer
     * @param log      log block, if not is null, add recipe: log -> planks * 4
     */
    @SuppressWarnings("JavadocReference")
    public void addRecipes(Consumer<FinishedRecipe> consumer, @Nullable Block log) {
        InventoryChangeTrigger.TriggerInstance hasLogs = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(ItemTags.LOGS).build());
        InventoryChangeTrigger.TriggerInstance hasPlank = InventoryChangeTrigger.TriggerInstance
                .hasItems(ItemPredicate.Builder.item().of(woodwork.planks()).build());
        EnterBlockTrigger.TriggerInstance inWater = EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.WATER);
        // planksFromLog
        if (log != null) {
            ShapelessRecipeBuilder.shapeless(woodwork.planks(), 4).group("planks")
                    .requires(log)
                    .unlockedBy("has_log", hasLogs)
                    .save(consumer);
        }

        // woodenBoat
        ShapedRecipeBuilder.shaped(woodwork.boat()).group("boat")
                .define('#', woodwork.planks())
                .pattern("# #")
                .pattern("###")
                .unlockedBy("in_water", inWater)
                .save(consumer);

        // chest
        if (woodwork.hasChest()) {
            ShapedRecipeBuilder.shaped(woodwork.chest()).group("chest")
                    .define('#', woodwork.planks())
                    .pattern("###")
                    .pattern("# #")
                    .pattern("###")
                    .unlockedBy("has_planks", hasPlank)
                    .save(consumer);
        }

        RecipeProvider.generateRecipes(consumer, new BlockFamily.Builder(woodwork.planks())
                .button(woodwork.button())
                .fence(woodwork.fence())
                .fenceGate(woodwork.fenceGate())
                .pressurePlate(woodwork.pressurePlate())
                .sign(woodwork.sign(), woodwork.wallSign())
                .slab(woodwork.slab())
                .stairs(woodwork.stairs())
                .door(woodwork.door())
                .trapdoor(woodwork.trapdoor())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .dontGenerateModel()
                .getFamily());
    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param provider tag provider
     */
    @SuppressWarnings("JavadocReference")
    public void addBlockTags(TagsProvider<Block> provider) {
        provider.tag(BlockTags.PLANKS).add(woodwork.planks());
        provider.tag(BlockTags.WOODEN_BUTTONS).add(woodwork.button());
        provider.tag(BlockTags.WOODEN_DOORS).add(woodwork.door());
        provider.tag(BlockTags.WOODEN_STAIRS).add(woodwork.stairs());
        provider.tag(BlockTags.WOODEN_SLABS).add(woodwork.slab());
        provider.tag(BlockTags.WOODEN_FENCES).add(woodwork.fence());
        provider.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(woodwork.pressurePlate());
        provider.tag(BlockTags.WOODEN_TRAPDOORS).add(woodwork.trapdoor());
        provider.tag(BlockTags.STANDING_SIGNS).add(woodwork.sign());
        provider.tag(BlockTags.WALL_SIGNS).add(woodwork.wallSign());
        provider.tag(BlockTags.FENCE_GATES).add(woodwork.fenceGate());
        provider.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(woodwork.fenceGate());
        if (woodwork.hasChest()) {
            provider.tag(Tags.Blocks.CHESTS_WOODEN).add(woodwork.chest());
        }

    }

    /**
     * call this method in {@link TagsProvider#addTags} or other equivalent method.
     *
     * @param provider tag provider
     */
    @SuppressWarnings("JavadocReference")
    public void addItemTags(TagsProvider<Item> provider) {
        provider.tag(ItemTags.BOATS).add(woodwork.boat());
    }

    /**
     * call this method in {@link LootTableProvider#getTables()} or other equivalent method.
     *
     * @param consumer consumer to collect table builder
     * @return block loot for add tree
     */
    @SuppressWarnings("JavadocReference")
    public WoodworkBlockLoot addLoots(Consumer<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> consumer) {
        WoodworkBlockLoot loot = new WoodworkBlockLoot(woodwork);
        consumer.accept(Pair.of(() -> loot, LootContextParamSets.BLOCK));
        return loot;
    }
}
