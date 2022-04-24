package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.WarnBlockStateProvider;
import com.example.examplemod.test.Registers;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends WarnBlockStateProvider {

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        TreeTest.TREE.register().addBlockModels(this);
        WoodworkTest.WOODWORK_WITH_CHEST.register().addBlockModels(this);
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addBlockModels(this);
        new ForDeferredRegister<>(Registers.BLOCKS)
                .skipAll(TreeTest.TREE.allBlocks())
                .skipAll(WoodworkTest.WOODWORK_WITH_CHEST.allBlocks())
                .skipAll(WoodworkTest.WOODWORK_WITHOUT_CHEST.allBlocks())
                .forDefault(block -> simpleBlock(block.get()))
                .addAll();
    }
}
