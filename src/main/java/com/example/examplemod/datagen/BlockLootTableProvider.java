package com.example.examplemod.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BlockLootTableProvider extends LootTableProvider {

    public BlockLootTableProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    protected abstract void createTables(List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> list);

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> list = new ArrayList<>();
        createTables(list);
        return list;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
        map.forEach((location, lootTable) -> LootTables.validate(context, location, lootTable));
    }
}
