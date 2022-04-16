package com.example.examplemod.test.tree;

import com.example.examplemod.feature.TreeFeatureBuilder;
import com.example.examplemod.test.ModTab;
import com.example.examplemod.tree.Tree;
import com.example.examplemod.tree.TreeSaplingGrower;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

import static com.example.examplemod.ExampleMod.MOD_ID;
import static com.example.examplemod.test.Registers.BLOCKS;
import static com.example.examplemod.test.Registers.ITEMS;

public class TreeTest {

    public static final Tree TREE = Tree.builder(MOD_ID, "test_tree")
            .tab(ModTab.INNSTANCE)
            .grower(new TreeSaplingGrower(tree -> new TreeFeatureBuilder()
                .dirt(BlockStateProvider.simple(Blocks.OAK_WOOD))
                .foliage(BlockStateProvider.simple(tree.leaves()))
                .foliagePlacer(new DarkOakFoliagePlacer(ConstantInt.ZERO, ConstantInt.ZERO))
                .trunk(BlockStateProvider.simple(tree.log()))
                .trunkPlacer(new DarkOakTrunkPlacer(6, 2, 1))
                .minimumSize(new TwoLayersFeatureSize(2, 0, 2))
                .addModifier(PlacementUtils.filteredByBlockSurvival(TreeTest.TREE.sapling()))
                .configured(tree.name())))
            .build(BLOCKS, ITEMS);

    public static void register(IEventBus bus) {
        bus.addListener((Consumer<FMLClientSetupEvent>) event -> TREE.register().registerRenderType());
    }
}
