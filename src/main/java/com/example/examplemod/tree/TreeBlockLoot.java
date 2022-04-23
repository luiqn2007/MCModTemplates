package com.example.examplemod.tree;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * A loot table for tree blocks
 */
public class TreeBlockLoot extends BlockLoot {
    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    private final Tree tree;
    private final Set<Block> addedBlocks = new HashSet<>();

    public TreeBlockLoot(Tree tree) {
        this.tree = tree;
    }

    @Override
    protected void addTables() {
        addDrop(tree.sapling(), BlockLoot::createSingleItemTable);
        addDrop(tree.log(), BlockLoot::createSingleItemTable);
        addDrop(tree.strippedLog(), BlockLoot::createSingleItemTable);
        addDrop(tree.strippedLog(), BlockLoot::createSingleItemTable);
        addDrop(tree.wood(), BlockLoot::createSingleItemTable);
        addDrop(tree.strippedWoods(), BlockLoot::createSingleItemTable);
        addDrop(tree.leaves(), b -> BlockLoot.createLeavesDrops(b, tree.sapling(), NORMAL_LEAVES_SAPLING_CHANCES));
        addDrop(tree.pottedSapling(), b -> BlockLoot.createPotFlowerItemTable(tree.sapling()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return addedBlocks;
    }

    /**
     * Return added blocks by the loot table, use for block filter
     *
     * @return blocks
     */
    public Set<Block> knownBlocks() {
        return Set.copyOf(addedBlocks);
    }

    private void addDrop(Block block, Function<Block, LootTable.Builder> drop) {
        if (block instanceof ILootable lootable) {
            add(block, lootable.createLootBuilder());
        } else {
            add(block, drop);
        }
        addedBlocks.add(block);
    }
}
