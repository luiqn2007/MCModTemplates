package com.example.examplemod.woodwork.with_mixin;

import net.minecraft.world.level.block.Block;

public interface IBlockEntityType {
    void addBlockToEntity(Block... blocks);
}
