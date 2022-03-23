package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.BlockLootTableProvider;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockLootProvider extends BlockLootTableProvider {

    public BlockLootProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void createTables(List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> list) {
        TreeTest.TREE.register().addLoots(list::add);
        WoodworkTest.TEST_AT.register().addLoots(list::add);
        WoodworkTest.TEST_MIXIN.register().addLoots(list::add);
    }
}
