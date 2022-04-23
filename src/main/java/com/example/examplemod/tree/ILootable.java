package com.example.examplemod.tree;

import net.minecraft.world.level.storage.loot.LootTable;

public interface ILootable {

    LootTable.Builder createLootBuilder();
}
