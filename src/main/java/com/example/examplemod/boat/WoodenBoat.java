package com.example.examplemod.boat;

import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WoodenBoat extends BoatItem {

    private static final List<RegistryObject<WoodenBoat>> BOATS = new LinkedList<>();
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private static final Logger LOGGER = LogManager.getLogger();

    public static RegistryObject<WoodenBoat> of(ResourceLocation name, CreativeModeTab tab, Supplier<? extends Block> planks,
                                                DeferredRegister<Item> items, DeferredRegister<EntityType<?>> entities) {
        String rName = name.getPath() + "_boat";
        RegistryObject<EntityType<WoodenBoatEntity>> entity = entities.register(rName, () -> EntityType.Builder
                .<WoodenBoatEntity>of(WoodenBoatEntity::new, MobCategory.MISC)
                .sized(1.375F, 0.5625F)
                .clientTrackingRange(10)
                .build(rName));
        RegistryObject<WoodenBoat> item = items.register(rName, () -> {
            WoodenBoat boat = new WoodenBoat(name, tab, planks, entity);
            DispenserBlock.registerBehavior(boat, boat.dispenserBehavior());
            return boat;
        });
        BOATS.add(item);
        return item;
    }

    public static RegistryObject<WoodenBoat> of(String modid, String name, CreativeModeTab tab, Supplier<? extends Block> planks,
                                                DeferredRegister<Item> items, DeferredRegister<EntityType<?>> entities) {
        return of(new ResourceLocation(modid, name), tab, planks, items, entities);
    }

    public static RegistryObject<WoodenBoat> of(String name, CreativeModeTab tab, Supplier<? extends Block> planks,
                                                DeferredRegister<Item> items, DeferredRegister<EntityType<?>> entities) {
        return of(new ResourceLocation(name), tab, planks, items, entities);
    }

    public static Stream<RegistryObject<WoodenBoat>> stream() {
        return BOATS.stream();
    }

    public final ResourceLocation name;
    public final RegistryObject<EntityType<WoodenBoatEntity>> boatEntity;
    public final Supplier<? extends Block> planks;

    private final BoatRegisterHelper register = new BoatRegisterHelper();

    @Nullable
    Object boatLayer;

    WoodenBoat(ResourceLocation name, CreativeModeTab tab, Supplier<? extends Block> planks, RegistryObject<EntityType<WoodenBoatEntity>> boat) {
        super(Boat.Type.OAK, new Item.Properties().stacksTo(1).tab(tab));
        this.name = name;
        this.boatEntity = boat;
        this.planks = planks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vec3 = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vec31 = player.getEyePosition();

                for (Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (aabb.contains(vec31)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if (hitresult.getType() == HitResult.Type.BLOCK) {
                WoodenBoatEntity boat = new WoodenBoatEntity(this, level, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z, player.getYRot());
                if (!level.noCollision(boat, boat.getBoundingBox())) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(boat);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, new BlockPos(hitresult.getLocation()));
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }

    public EntityType<WoodenBoatEntity> entity() {
        return boatEntity.get();
    }

    public Block planks() {
        return planks.get();
    }

    public DefaultDispenseItemBehavior dispenserBehavior() {
        return new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
                Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
                Level level = pSource.getLevel();
                double x = pSource.x() + direction.getStepX() * 1.125F;
                double y = pSource.y() + direction.getStepY() * 1.125F;
                double z = pSource.z() + direction.getStepZ() * 1.125F;
                BlockPos blockpos = pSource.getPos().relative(direction);
                double dz;
                if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                    dz = 1.0D;
                } else {
                    if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
                        // DefaultDispenseItemBehavior#dispense
                        ItemStack itemstack = super.execute(pSource, pStack);
                        this.playSound(pSource);
                        this.playAnimation(pSource, pSource.getBlockState().getValue(DispenserBlock.FACING));
                        return itemstack;
                    }
                    dz = 0.0D;
                }

                WoodenBoatEntity boat = new WoodenBoatEntity(WoodenBoat.this, level, x, y + dz, z, direction.toYRot());
                level.addFreshEntity(boat);
                pStack.shrink(1);
                return pStack;
            }
        };
    }

    public BoatRegisterHelper register() {
        return register;
    }

    @OnlyIn(Dist.CLIENT)
    public net.minecraft.client.model.geom.ModelLayerLocation boatLayer() {
        return (net.minecraft.client.model.geom.ModelLayerLocation) Objects.requireNonNull(boatLayer);
    }

    public class BoatRegisterHelper {

        public static void registerClient(RegistryObject<WoodenBoat> boat, IEventBus bus) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.register(new Object() {
                @SubscribeEvent
                public void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
                    BoatRegisterHelper.registerLayers(boat, event);
                }

                @SubscribeEvent
                public void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
                    BoatRegisterHelper.registerRenderer(boat, event);
                }
            }));
        }

        /**
         * Call when {@link EntityRenderersEvent.RegisterLayerDefinitions} event fired
         */
        @OnlyIn(Dist.CLIENT)
        public static void registerLayers(RegistryObject<WoodenBoat> boat, EntityRenderersEvent.RegisterLayerDefinitions event) {
            ResourceLocation n = boat.get().name;
            ResourceLocation location = new ResourceLocation(n.getNamespace(), "boat/" + n.getPath());
            boat.get().boatLayer = new net.minecraft.client.model.geom.ModelLayerLocation(location, "main");
            event.registerLayerDefinition(boat.get().boatLayer(), net.minecraft.client.model.BoatModel::createBodyModel);
        }

        /**
         * Call when {@link EntityRenderersEvent.RegisterRenderers} event fired
         */
        @OnlyIn(Dist.CLIENT)
        public static void registerRenderer(RegistryObject<WoodenBoat> boat, EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(boat.get().entity(), WoodenBoatRenderer::new);
        }

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, add chinese name
         *
         * @param provider language provider
         * @param chinese  tree chinese name
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesZh(LanguageProvider provider, String chinese) {
            provider.add(WoodenBoat.this, chinese + "木船");
        }

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
         *
         * @param provider language provider
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesEn(LanguageProvider provider) {
            StringBuilder sb = new StringBuilder();
            for (String s : name.getPath().split("_")) {
                if (s.isEmpty()) continue;
                String s1 = s.toLowerCase(Locale.ROOT);
                sb.append(Character.toUpperCase(s1.charAt(0)));
                sb.append(s1.substring(1));
            }
            addLanguagesEn(provider, sb.toString());
        }

        /**
         * call this method in {@link LanguageProvider#addTranslations()} or other equivalent method, for english language
         *
         * @param provider language provider
         * @param english  english name
         */
        @SuppressWarnings("JavadocReference")
        public void addLanguagesEn(LanguageProvider provider, String english) {
            provider.add(WoodenBoat.this, english + " Boat");
        }

        /**
         * call this method in {@link ItemModelProvider#registerModels} or other equivalent method.
         *
         * @param provider item model provider
         */
        @SuppressWarnings("JavadocReference")
        public void addItemModels(ItemModelProvider provider) {
            ResourceLocation name = delegate.name();
            provider.singleTexture(name.getPath(), provider.mcLoc("item/generated"), "layer0", provider.modLoc("item/" + name.getPath()));
        }

        /**
         * call this method in {@link RecipeProvider#buildCraftingRecipes}
         * or other equivalent method.
         *
         * @param consumer save consumer
         */
        @SuppressWarnings("JavadocReference")
        public void addRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(WoodenBoat.this).group("boat")
                    .define('#', planks())
                    .pattern("# #")
                    .pattern("###")
                    .unlockedBy("in_water", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.WATER))
                    .save(consumer);
        }

        /**
         * call this method in {@link TagsProvider#addTags} or other equivalent method.
         *
         * @param tag tag provider
         */
        @SuppressWarnings("JavadocReference")
        public void addItemTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> tag) {
            tag.apply(ItemTags.BOATS).add(WoodenBoat.this);
        }

        /**
         * Call in somewhere can get ExistingFileHelper,
         * like {@link net.minecraftforge.forge.event.lifecycle.GatherDataEvent} event
         * @param helper file helper
         */
        public void verifyTexture(ExistingFileHelper helper) {
            ResourceLocation tex = new ResourceLocation(name.getNamespace(), "textures/entity/boat/" + name.getPath() + ".png");
            if (!helper.exists(tex, PackType.CLIENT_RESOURCES)) {
                LOGGER.warn("Not found boat skin at {}", tex);
            }
        }
    }
}
