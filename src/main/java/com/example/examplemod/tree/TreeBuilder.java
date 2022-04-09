package com.example.examplemod.tree;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class TreeBuilder {

    final ResourceLocation name;
    CreativeModeTab tab = CreativeModeTab.TAB_DECORATIONS;
    SoundType sound = SoundType.GRASS;
    MaterialColor topLogColor = MaterialColor.WOOD;
    MaterialColor barkLogColor = MaterialColor.WOOD;
    MaterialColor topStrippedLogColor = MaterialColor.WOOD;
    MaterialColor barkStrippedLogColor = MaterialColor.WOOD;
    MaterialColor plankColor = MaterialColor.WOOD;
    MaterialColor woodColor = MaterialColor.WOOD;
    MaterialColor strippedWoodColor = MaterialColor.WOOD;
    AbstractTreeGrower grower = new OakTreeGrower();

    Function<Tree, SaplingBlock> sapling = tree -> new SaplingBlock(tree.properties().grower(), BlockBehaviour.Properties.of(Material.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS));
    Function<Tree, BlockItem> saplingItem = tree -> new BlockItem(tree.sapling(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, RotatedPillarBlock> log = tree -> new RotatedPillarBlock(BlockBehaviour.Properties
            .of(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y
                    ? tree.properties().topLogColor()
                    : tree.properties().barkLogColor())
            .strength(2.0F)
            .sound(SoundType.WOOD));
    Function<Tree, BlockItem> logItem = tree -> new BlockItem(tree.log(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, RotatedPillarBlock> strippedLog = tree -> new RotatedPillarBlock(BlockBehaviour.Properties
            .of(Material.WOOD, state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y
                    ? tree.properties().topStrippedLogColor()
                    : tree.properties().barkStrippedLogColor())
            .strength(2.0F)
            .sound(SoundType.WOOD));
    Function<Tree, BlockItem> strippedLogItem = tree -> new BlockItem(tree.strippedLog(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, RotatedPillarBlock> wood = tree -> new RotatedPillarBlock(BlockBehaviour.Properties
            .of(Material.WOOD, tree.properties().woodColor())
            .strength(2.0F)
            .sound(SoundType.WOOD));
    Function<Tree, BlockItem> woodItem = tree -> new BlockItem(tree.wood(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, RotatedPillarBlock> strippedWoods = tree -> new RotatedPillarBlock(BlockBehaviour.Properties
            .of(Material.WOOD, tree.properties().strippedWoodColor())
            .strength(2.0F)
            .sound(SoundType.WOOD));
    Function<Tree, BlockItem> strippedWoodsItem = tree -> new BlockItem(tree.strippedWoods(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, LeavesBlock> leaves = tree -> new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F)
            .randomTicks()
            .sound(tree.properties().sound())
            .noOcclusion()
            .isValidSpawn((_1, _2, _3, entity) -> entity == EntityType.OCELOT || entity == EntityType.PARROT)
            .isSuffocating((_1, _2, _3) -> false)
            .isViewBlocking((_1, _2, _3) -> false));
    Function<Tree, BlockItem> leavesItem = tree -> new BlockItem(tree.leaves(), new Item.Properties().tab(tree.properties().tab()));

    Function<Tree, FlowerPotBlock> pottedSapling = tree -> new FlowerPotBlock(
            () -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), tree.sapling,
            BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion());

    public TreeBuilder(ResourceLocation name) {
        this.name = name;
    }

    public TreeBuilder tab(CreativeModeTab tab) {
        this.tab = tab;
        return this;
    }

    /**
     * Leaves: set leaves sound, all vanilla leaves is {@link SoundType#GRASS},
     * except azalea is {@link  SoundType#AZALEA_LEAVES}
     *
     * @param sound leave sound, default is {@link SoundType#GRASS}
     * @return this builder
     */
    public TreeBuilder leavesSound(SoundType sound) {
        this.sound = sound;
        return this;
    }

    /**
     * Log: set log color in map
     *
     * @param top  top color, default is {@link MaterialColor#WOOD}
     * @param bark bark color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder logColor(MaterialColor top, MaterialColor bark) {
        this.topLogColor = top;
        this.barkLogColor = bark;
        return this;
    }

    /**
     * Log: set stripped log color in map
     *
     * @param top  top color, default is {@link MaterialColor#WOOD}
     * @param bark bark color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder strippedLogColor(MaterialColor top, MaterialColor bark) {
        this.topStrippedLogColor = top;
        this.barkStrippedLogColor = bark;
        return this;
    }

    /**
     * Wood: set wood color in map
     *
     * @param color wood color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder woodColor(MaterialColor color) {
        this.woodColor = color;
        return this;
    }

    /**
     * Plank: set stripped wood color in map
     *
     * @param color wood color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder strippedWoodColor(MaterialColor color) {
        this.strippedWoodColor = color;
        return this;
    }

    /**
     * Plank: set wood color in map
     *
     * @param normal   wood color, default is {@link MaterialColor#WOOD}
     * @param stripped stripped plank color, default is {@link MaterialColor#WOOD}
     * @return this builder
     */
    public TreeBuilder woodColor(MaterialColor normal, MaterialColor stripped) {
        woodColor(normal);
        strippedWoodColor(stripped);
        return this;
    }

    /**
     * Set all log color in map
     *
     * @param topColor      top log color
     * @param woodColor     wood color
     * @param barkColor     bark log color
     * @param strippedColor stripped log color
     * @return this builder
     */
    public TreeBuilder color(MaterialColor topColor, MaterialColor woodColor, MaterialColor barkColor, MaterialColor strippedColor) {
        logColor(topColor, barkColor);
        strippedLogColor(topColor, strippedColor);
        woodColor(woodColor, strippedColor);
        return this;
    }

    /**
     * Set all log color in map
     *
     * @param woodColor     top log color, plank color and wood color
     * @param barkColor     bark log color
     * @param strippedColor stripped log color
     * @return this builder
     */
    public TreeBuilder color(MaterialColor woodColor, MaterialColor barkColor, MaterialColor strippedColor) {
        return color(woodColor, woodColor, barkColor, strippedColor);
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param grower tree grower
     * @return this builder
     */
    public TreeBuilder grower(AbstractTreeGrower grower) {
        this.grower = grower;
        return this;
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature selector
     * @return this builder
     */
    public TreeBuilder grower(Function<Random, ConfiguredFeature<?, ?>> feature) {
        this.grower = new AbstractTreeGrower() {
            private Holder<ConfiguredFeature<?, ?>> configuredFeature = null;
            private boolean initialized = false;

            @Nullable
            @Override
            protected Holder<ConfiguredFeature<?, ?>> getConfiguredFeature(Random pRandom, boolean pLargeHive) {
                if (!initialized) {
                    configuredFeature = Holder.direct(feature.apply(pRandom));
                    initialized = true;
                }
                return configuredFeature;
            }
        };
        return this;
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature getter
     * @return this builder
     */
    public TreeBuilder grower(Supplier<ConfiguredFeature<?, ?>> feature) {
        return grower(r -> feature.get());
    }

    /**
     * Sapling: set tree grower, use to grow the tree
     *
     * @param feature feature
     * @return this builder
     */
    public TreeBuilder grower(ConfiguredFeature<?, ?> feature) {
        return grower(r -> feature);
    }

    public TreeBuilder customSapling(Function<Tree, SaplingBlock> factory) {
        this.sapling = factory;
        return this;
    }

    public TreeBuilder customSaplingItem(Function<Tree, BlockItem> factory) {
        this.saplingItem = factory;
        return this;
    }

    public TreeBuilder customLog(Function<Tree, RotatedPillarBlock> factory) {
        this.log = factory;
        return this;
    }

    public TreeBuilder customLogItem(Function<Tree, BlockItem> factory) {
        this.logItem = factory;
        return this;
    }

    public TreeBuilder customStrippedLog(Function<Tree, RotatedPillarBlock> factory) {
        this.strippedLog = factory;
        return this;
    }

    public TreeBuilder customStrippedLogItem(Function<Tree, BlockItem> factory) {
        this.strippedLogItem = factory;
        return this;
    }

    public TreeBuilder customWood(Function<Tree, RotatedPillarBlock> factory) {
        this.wood = factory;
        return this;
    }

    public TreeBuilder customWoodItem(Function<Tree, BlockItem> factory) {
        this.woodItem = factory;
        return this;
    }

    public TreeBuilder customStrippedWood(Function<Tree, RotatedPillarBlock> factory) {
        this.strippedWoods = factory;
        return this;
    }

    public TreeBuilder customStrippedWoodItem(Function<Tree, BlockItem> factory) {
        this.strippedWoodsItem = factory;
        return this;
    }

    public TreeBuilder customLeaves(Function<Tree, LeavesBlock> factory) {
        this.leaves = factory;
        return this;
    }

    public TreeBuilder customLeavesItem(Function<Tree, BlockItem> factory) {
        this.leavesItem = factory;
        return this;
    }

    public TreeBuilder customPottedSapling(Function<Tree, FlowerPotBlock> factory) {
        this.pottedSapling = factory;
        return this;
    }

    public Tree build(DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
        return new Tree(this, blocks, items);
    }
}
