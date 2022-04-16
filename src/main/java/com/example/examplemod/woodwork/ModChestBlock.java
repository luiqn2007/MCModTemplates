package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class ModChestBlock extends ChestBlock {

    private final Woodwork woodwork;

    protected ModChestBlock(Woodwork woodwork, Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> typeSupplier) {
        super(properties, typeSupplier);
        this.woodwork = woodwork;
    }

    protected ModChestBlock(Woodwork woodwork) {
        super(Properties.copy(Blocks.CHEST).color(woodwork.plankColor), WoodworkManager::chestBlockEntityType);
        this.woodwork = woodwork;
    }

    public Woodwork getWoodwork() {
        return woodwork;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModChestBlockEntity(pPos, pState);
    }
}
