package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModChestTrappedBlockEntity extends ModChestBlockEntity {

    protected ModChestTrappedBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState state) {
        super(type, blockPos, state);
    }

    public ModChestTrappedBlockEntity(BlockPos blockPos, BlockState state) {
        this(WoodworkManager.trappedChestBlockEntityType(), blockPos, state);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int prevCount, int count) {
        super.signalOpenCount(level, pos, state, prevCount, count);
        if (prevCount != count) {
            Block block = state.getBlock();
            level.updateNeighborsAt(pos, block);
            level.updateNeighborsAt(pos.below(), block);
        }
    }

    @Override
    public boolean isTrapped() {
        return true;
    }
}
