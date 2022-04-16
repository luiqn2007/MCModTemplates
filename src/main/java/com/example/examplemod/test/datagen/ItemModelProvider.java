package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.UnexceptionalItemModelProvider;
import com.example.examplemod.test.Registers;
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
        TreeTest.TREE.register().addItemModels(this);
        WoodworkTest.WOODWORK_WITH_CHEST.register().addItemModels(this);
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addItemModels(this);
        new ForDeferredRegister<>(Registers.ITEMS)
                .skipAll(TreeTest.TREE.allItems())
                .skipAll(WoodworkTest.WOODWORK_WITH_CHEST.allItems())
                .skipAll(WoodworkTest.WOODWORK_WITHOUT_CHEST.allItems())
                .forDefault(this::block)
                .addAll();
    }
}
