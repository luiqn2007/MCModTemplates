package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.WarnBlockStateProvider;
import com.example.examplemod.test.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends WarnBlockStateProvider {

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        TestTree.TREE.register().addBlockModels(this);
        TestWoodwork.WOODWORK1.register().addBlockModels(this);
        TestWoodwork.WOODWORK2.register().addBlockModels(this);
        TestChest.CHEST1.get().register().addBlockModels(this);
        TestChest.TRAPPED_CHEST1.get().register().addBlockModels(this);
        TestChest.CHEST2.get().register().addBlockModels(this);
        TestChest.CHEST3.get().register().addBlockModels(this);
        new ForDeferredRegister<>(Registers.BLOCKS)
                .skipAll(TestTree.TREE.allBlocks())
                .skipAll(TestWoodwork.WOODWORK1.allBlocks())
                .skipAll(TestWoodwork.WOODWORK2.allBlocks())
                .skip(TestChest.CHEST1)
                .skip(TestChest.TRAPPED_CHEST1)
                .skip(TestChest.CHEST2)
                .skip(TestChest.CHEST3)
                .forDefault(block -> simpleBlock(block.get()))
                .addAll();
    }
}
