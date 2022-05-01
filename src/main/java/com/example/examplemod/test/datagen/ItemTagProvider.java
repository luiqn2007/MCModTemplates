package com.example.examplemod.test.datagen;

import com.example.examplemod.test.TestBoat;
import com.example.examplemod.test.TestChest;
import com.example.examplemod.test.TestTree;
import com.example.examplemod.test.TestWoodwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(DataGenerator generator, BlockTagsProvider bp, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, bp, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TestTree.TREE.register().addItemTags(this::tag);
        TestWoodwork.WOODWORK1.register().addItemTags(this::tag);
        TestWoodwork.WOODWORK2.register().addItemTags(this::tag);
        TestChest.CHEST1.get().register().addItemTags(this::tag);
        TestChest.TRAPPED_CHEST1.get().register().addItemTags(this::tag);
        TestChest.CHEST2.get().register().addItemTags(this::tag);
        TestChest.CHEST3.get().register().addItemTags(this::tag);
        TestBoat.BOAT.get().register().addItemTags(this::tag);
    }
}
