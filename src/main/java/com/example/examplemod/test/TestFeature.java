package com.example.examplemod.test;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.feature.OreFeatureBuilder;
import com.example.examplemod.feature.TreeFeatureBuilder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TestFeature {

    public static final Lazy<OreFeatureBuilder> ORE_TEST = Lazy.of(() -> new OreFeatureBuilder()
        .fromModifier(OrePlacements.ORE_COPPER)
        .fromConfiguration(OreFeatures.ORE_COPPER_LARGE.value().config())
        .addReplaceRule(new TagMatchTest(BlockTags.REPLACEABLE_PLANTS), Blocks.DIAMOND_BLOCK));

    public static final Lazy<TreeFeatureBuilder> TREE_TEST = Lazy.of(() -> new TreeFeatureBuilder()
        .dirt(BlockStateProvider.simple(Blocks.DIAMOND_ORE))
        .foliage(BlockStateProvider.simple(Blocks.ACACIA_LEAVES))
        .foliagePlacer(new DarkOakFoliagePlacer(ConstantInt.ZERO, ConstantInt.ZERO))
        .trunk(BlockStateProvider.simple(Blocks.BEE_NEST))
        .trunkPlacer(new DarkOakTrunkPlacer(6, 2, 1))
        .minimumSize(new TwoLayersFeatureSize(2, 0, 2)));

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onGenerator(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        generation.addFeature(Decoration.UNDERGROUND_ORES, ORE_TEST.get().build(ExampleMod.MOD_ID, "test_ore"));
        generation.addFeature(Decoration.VEGETAL_DECORATION, TREE_TEST.get().build(ExampleMod.MOD_ID, "test_tree"));
    }
}
