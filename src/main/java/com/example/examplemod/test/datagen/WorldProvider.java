package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.WorldGenProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

import static net.minecraft.world.level.levelgen.SurfaceRules.state;

public class WorldProvider extends WorldGenProvider {

    public WorldProvider(DataGenerator generator, String modid) {
        super(generator, modid, true);
    }

    @Override
    protected void addAll() {
        ResourceLocation dimensionType = addDimensionType(modLoc("test_dimension"),
                DimensionType.create(OptionalLong.empty(), false, false, false,
                        false, 1.0, false, false, false,
                        false, false, 0, 16, 16,
                        BlockTags.INFINIBURN_OVERWORLD, DimensionType.OVERWORLD_EFFECTS, 1));
        ResourceLocation carverAir = addConfiguredCarver(modLoc("test_carver_air"),
                new ConfiguredWorldCarver<>(WorldCarver.CAVE, new CaveCarverConfiguration(0.8f,
                        ConstantHeight.of(VerticalAnchor.absolute(5)), ConstantFloat.of(1.9f), VerticalAnchor.absolute(2),
                        false, ConstantFloat.of(1.5f), ConstantFloat.of(1.2f), ConstantFloat.of(0))));
        ResourceLocation carverLiquid = addConfiguredCarver(modLoc("test_carver_liquid"),
                new ConfiguredWorldCarver<>(WorldCarver.CAVE, new CaveCarverConfiguration(0.7f,
                        ConstantHeight.of(VerticalAnchor.absolute(0)), ConstantFloat.of(2.3f), VerticalAnchor.absolute(3),
                        true, ConstantFloat.of(1.3f), ConstantFloat.of(1.1f), ConstantFloat.of(0))));
        ResourceLocation feature = addConfiguredFeature(modLoc("test_feature"),
                new ConfiguredFeature<>(Feature.FLOWER, new RandomPatchConfiguration(5, 10, 10,
                        getPlacedFeature(modLoc("test_placed")))));
        ResourceLocation placedFeature = addPlacedFeature(modLoc("test_placed"),
                new PlacedFeature(getConfiguredFeature(feature), List.of(
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(5)),
                        BiomeFilter.biome(),
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockTags.STONE_ORE_REPLACEABLES)),
                        CountPlacement.of(10))));
        ResourceLocation biome = addBiome(modLoc("test_biome"), new Biome.BiomeBuilder()
                .biomeCategory(Biome.BiomeCategory.DESERT)
                .downfall(1)
                .generationSettings(new BiomeGenerationSettings.Builder()
                        .addCarver(GenerationStep.Carving.AIR, getConfiguredCarver(carverAir))
                        .addCarver(GenerationStep.Carving.LIQUID, getConfiguredCarver(carverLiquid))
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, getPlacedFeature(placedFeature))
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder()
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 1, 3, 10))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CAT, 5, 10, 50))
                        .addMobCharge(EntityType.CREEPER, 5, 3)
                        .addMobCharge(EntityType.ZOMBIE, 3, 3)
                        .build())
                .precipitation(Biome.Precipitation.SNOW)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0xFF1234)
                        .fogColor(0xFF5678)
                        .skyColor(0xFF9012)
                        .waterFogColor(0xFF3456)
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.FISHING, 3))
                        .grassColorOverride(0xFF8520)
                        .foliageColorOverride(0xFF0258)
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST)
                        .backgroundMusic(Musics.END)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .ambientLoopSound(SoundEvents.AMBIENT_UNDERWATER_LOOP)
                        .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 3))
                        .build())
                .temperature(100)
                .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
                .build());
        ResourceLocation paramOne = addNoiseParameter(modLoc("one"), new NormalNoise.NoiseParameters(1, 1));
        ResourceLocation funcBarrier = addDensityFunction(modLoc("test_barrier"), DensityFunctions.noise(getNoiseParameter(paramOne)));
        ResourceLocation funcFluid = addDensityFunction(modLoc("test_fluid"), DensityFunctions.noise(getNoiseParameter(paramOne)));
        ResourceLocation noiseGeneratorSettings = addNoiseGeneratorSettings(modLoc("test_noise_settings"), new NoiseGeneratorSettings(
                new NoiseSettings(0, 16, new NoiseSamplingSettings(1, 1, 1, 1),
                        new NoiseSlider(1, 1, 1),
                        new NoiseSlider(1, 1, 1), 1, 1,
                        TerrainShaper.overworld(true)),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                new NoiseRouterWithOnlyNoises(
                        getDensityFunction(funcBarrier).value(),
                        getDensityFunction(funcFluid).value(),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0),
                        DensityFunctions.constant(0)),
                sequence().ifAbovePreliminarySurface(state(Blocks.STONE.defaultBlockState())).build(), 0,
                false, false, false, false));
        ResourceLocation list = addProcessorList(modLoc("test_processor_list"),
                new StructureProcessorList(List.of(new ProtectedBlockProcessor(BlockTags.DOORS))));
        ResourceLocation template = addTemplatePool(modLoc("test_template"), new StructureTemplatePool(
                modLoc("test_template"), modLoc("test_template"), List.of(Pair.of(
                        SinglePoolElement.single("test_element", getProcessorList(list)).apply(StructureTemplatePool.Projection.RIGID), 1))));
        ResourceLocation structureFeature1 = addConfiguredStructureFeature(modLoc("configured_structure_feature1"), new ConfiguredStructureFeature<>(
                StructureFeature.END_CITY, NoneFeatureConfiguration.INSTANCE, HolderSet.direct(getBiome(biome)), true, Map.of(
                MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, WeightedRandomList.create(
                        new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 3, 5),
                        new MobSpawnSettings.SpawnerData(EntityType.COW, 2, 4, 6))))));
        ResourceLocation structureFeature2 = addConfiguredStructureFeature(modLoc("configured_structure_feature2"), new ConfiguredStructureFeature<>(
                StructureFeature.VILLAGE,
                new JigsawConfiguration(getTemplatePool(template), 5),
                HolderSet.direct(getBiome(modLoc("another_biome"))), false, Map.of()));
        ResourceLocation structureSet1 = addStructureSet(modLoc("test_structure_set_1"),
                new StructureSet(getConfiguredStructureFeature(structureFeature1),
                        new RandomSpreadStructurePlacement(1, 1, RandomSpreadType.LINEAR, 1)));
        ResourceLocation structureSet2 = addStructureSet(modLoc("test_structure_set_2"),
                new StructureSet(getConfiguredStructureFeature(structureFeature2), new ConcentricRingsStructurePlacement(1, 2, 3)));
        addDimension(modLoc("test_dimension_1"), new LevelStem(getDimensionType(dimensionType), new NoiseBasedChunkGenerator(
                structureSets, noiseParameters, new FixedBiomeSource(getBiome(biome)), 0,
                getNoiseGeneratorSettings(noiseGeneratorSettings))));
        addDimension(modLoc("test_dimension_2"), new LevelStem(getDimensionType(dimensionType), new FlatLevelSource(
                structureSets, new FlatLevelGeneratorSettings(Optional.of(
                HolderSet.direct(getStructureSet(structureSet1), getStructureSet(structureSet2))), biomes))));
    }
}
