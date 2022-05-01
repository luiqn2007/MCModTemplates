package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.BlockLootTableProvider;
import com.example.examplemod.datagen.RegisteredBlockLoot;
import com.example.examplemod.test.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockLootProvider extends BlockLootTableProvider {

    public BlockLootProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void createTables(List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> list) {
        Set<Block> skip1 = TestTree.TREE.register().addLoots(list::add).knownBlocks();
        Set<Block> skip2 = TestWoodwork.WOODWORK1.register().addLoots(list::add).knownBlocks();
        Set<Block> skip3 = TestWoodwork.WOODWORK2.register().addLoots(list::add).knownBlocks();
        TestChest.CHEST1.get().register().addLoots(list::add);
        TestChest.TRAPPED_CHEST1.get().register().addLoots(list::add);
        TestChest.CHEST2.get().register().addLoots(list::add);
        TestChest.CHEST3.get().register().addLoots(list::add);
        list.add(new RegisteredBlockLoot(Registers.BLOCKS)
                .skipAll(skip1)
                .skipAll(skip2)
                .skipAll(skip3)
                .skip(TestChest.CHEST1)
                .skip(TestChest.TRAPPED_CHEST1)
                .skip(TestChest.CHEST2)
                .skip(TestChest.CHEST3)
                .toPair());
    }
}
