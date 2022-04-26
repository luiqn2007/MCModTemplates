package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class ModChestTrappedBlock extends ModChestBlock {

    protected ModChestTrappedBlock(Woodwork woodwork, Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> typeSupplier) {
        super(woodwork, properties, typeSupplier);
    }

    public ModChestTrappedBlock(Properties properties, Woodwork woodwork) {
        super(woodwork, properties, WoodworkManager::trappedChestBlockEntityType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModChestTrappedBlockEntity(pPos, pState);
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(pBlockAccess, pPos), 0, 15);
    }

    @Override
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pSide == Direction.UP ? pBlockState.getSignal(pBlockAccess, pPos, pSide) : 0;
    }
}
