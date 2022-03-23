package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.UnexceptionalItemModelProvider;
import com.example.examplemod.test.item.axe.stripping.StripTest;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends UnexceptionalItemModelProvider {

    public ItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper helper) {
        super(generator, modid, helper);
    }

    @Override
    protected void registerModels() {
        new ForDeferredRegister<>(StripTest.ITEMS)
                .forDefault(this::block)
                .addAll();
        TreeTest.TREE.register().addItemModels(this);
        WoodworkTest.TEST_AT.register().addItemModels(this);
        WoodworkTest.TEST_MIXIN.register().addItemModels(this);
    }
}
