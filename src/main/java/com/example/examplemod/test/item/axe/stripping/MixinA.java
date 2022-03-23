package com.example.examplemod.test.item.axe.stripping;

import com.example.examplemod.item.axe.stripping.with_mixin.IStrippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MixinA extends Block implements IStrippable {

    public MixinA() {
        super(Properties.copy(Blocks.STONE));
    }

    @Override
    public boolean canStripped() {
        return true;
    }

    @Override
    public BlockState getStrippedBlock() {
        return StripTest.BLOCK_M_STRIPPED.get().defaultBlockState();
    }
}
