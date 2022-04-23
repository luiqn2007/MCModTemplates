package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ModChestBlockEntity extends ChestBlockEntity implements IWoodworkEntity {

    protected ModChestBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState state) {
        super(type, blockPos, state);
    }

    public ModChestBlockEntity(BlockPos blockPos, BlockState state) {
        this(WoodworkManager.chestBlockEntityType(), blockPos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();
        return new AABB(pos.offset(-1, 0, -1), pos.offset(2, 2, 2));
    }

    public boolean isTrapped() {
        return false;
    }
}
