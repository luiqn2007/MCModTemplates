package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.UnexceptionalBlockStateProvider;
import com.example.examplemod.test.item.axe.stripping.StripTest;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends UnexceptionalBlockStateProvider {

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        new ForDeferredRegister<>(StripTest.BLOCKS)
                .forDefault(block -> simpleBlock(block.get()))
                .addAll();
        TreeTest.TREE.register().addBlockModels(this);
        WoodworkTest.TEST_AT.register().addBlockModels(this);
        WoodworkTest.TEST_MIXIN.register().addBlockModels(this);
    }
}
