package com.example.examplemod.mixin.woodwork;

import com.example.examplemod.woodwork.with_mixin.IBlockEntityType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(BlockEntityType.class)
public abstract class MixinBlockEntityType implements IBlockEntityType {

    @Shadow
    @Final
    @Mutable
    private Set<Block> validBlocks;

    /**
     * Add a method to append block to existed valid block
     *
     * @param blocks blocks to append to this type
     */
    @Override
    public void addBlockToEntity(Block... blocks) {
        ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
        builder.addAll(validBlocks);
        builder.add(blocks);
        validBlocks = builder.build();
    }
}
