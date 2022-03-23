package com.example.examplemod.woodwork.with_at;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * A loot table for tree blocks
 */
public class WoodworkBlockLoot extends BlockLoot {
    private final Woodwork woodwork;
    public final Set<Block> addedBlocks = new HashSet<>();

    public WoodworkBlockLoot(Woodwork woodwork) {
        this.woodwork = woodwork;
    }

    @Override
    protected void addTables() {
        addDrop(woodwork.planks(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.sign(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.wallSign(), b -> BlockLoot.createSingleItemTable(woodwork.sign()));
        addDrop(woodwork.pressurePlate(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.trapdoor(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.stairs(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.button(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.slab(), BlockLoot::createSlabItemTable);
        addDrop(woodwork.fenceGate(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.fence(), BlockLoot::createSingleItemTable);
        addDrop(woodwork.door(), BlockLoot::createDoorTable);
        if (woodwork.hasChest()) {
            addDrop(woodwork.chest(), BlockLoot::createNameableBlockEntityTable);
        }
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

    public interface ILootable {
        LootTable.Builder createLootBuilder();
    }
}
