package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.UnexceptionalBlockStateProvider;
import com.example.examplemod.test.item.axe.stripping.StripTest;
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
    }
}
