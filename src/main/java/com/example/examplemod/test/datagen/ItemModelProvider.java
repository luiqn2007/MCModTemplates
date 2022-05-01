package com.example.examplemod.test.datagen;

import com.example.examplemod.datagen.ForDeferredRegister;
import com.example.examplemod.datagen.WarnItemModelProvider;
import com.example.examplemod.test.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends WarnItemModelProvider {

    public ItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper helper) {
        super(generator, modid, helper);
    }

    @Override
    protected void registerModels() {
        TestTree.TREE.register().addItemModels(this);
        TestWoodwork.WOODWORK1.register().addItemModels(this);
        TestWoodwork.WOODWORK2.register().addItemModels(this);
        TestChest.CHEST1.get().register().addItemModels(this);
        TestChest.TRAPPED_CHEST1.get().register().addItemModels(this);
        TestChest.CHEST2.get().register().addItemModels(this);
        TestChest.CHEST3.get().register().addItemModels(this);
        TestBoat.BOAT.get().register().addItemModels(this);
        new ForDeferredRegister<>(Registers.ITEMS)
                .skipAll(TestTree.TREE.allItems())
                .skipAll(TestWoodwork.WOODWORK1.allItems())
                .skipAll(TestWoodwork.WOODWORK2.allItems())
                .skip(TestChest.CHEST1.get().asItem())
                .skip(TestChest.TRAPPED_CHEST1.get().asItem())
                .skip(TestChest.CHEST2.get().asItem())
                .skip(TestChest.CHEST3.get().asItem())
                .skip(TestBoat.BOAT)
                .forDefault(this::block)
                .addAll();
    }
}
