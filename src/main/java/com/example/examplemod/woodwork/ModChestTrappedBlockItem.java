package com.example.examplemod.woodwork;

import net.minecraft.world.level.block.Block;

public class ModChestTrappedBlockItem extends ModChestBlockItem {

    protected ModChestTrappedBlockItem(Woodwork woodwork, Block block, Properties properties) {
        super(woodwork, block, properties);
    }

    public ModChestTrappedBlockItem(Properties properties, Woodwork woodwork) {
        this(woodwork, woodwork.trappedChest(), properties);
    }
}
