package com.example.examplemod.block.chest;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WoodenChestEntity extends ChestBlockEntity {

    public final boolean isTrapped;
    public final ResourceLocation name;

    public WoodenChestEntity(WoodenChest.ChestHolder holder, BlockPos blockPos, BlockState state) {
        super(holder.entity.get(), blockPos, state);
        this.isTrapped = holder.isTrapped;
        this.name = holder.name;
    }

    @Override
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();
        return new AABB(pos.offset(-1, 0, -1), pos.offset(2, 2, 2));
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int prevCount, int count) {
        super.signalOpenCount(level, pos, state, prevCount, count);
        if (isTrapped && prevCount != count) {
            Block block = state.getBlock();
            level.updateNeighborsAt(pos, block);
            level.updateNeighborsAt(pos.below(), block);
        }
    }
}
