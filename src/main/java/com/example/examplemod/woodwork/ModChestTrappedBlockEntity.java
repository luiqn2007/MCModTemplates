package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModChestTrappedBlockEntity extends ModChestBlockEntity {

    protected ModChestTrappedBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState state) {
        super(type, blockPos, state);
    }

    public ModChestTrappedBlockEntity(BlockPos blockPos, BlockState state) {
        this(WoodworkManager.trappedChestBlockEntityType(), blockPos, state);
    }


}
