package com.example.examplemod.test.datagen;

import com.example.examplemod.test.TestChest;
import com.example.examplemod.test.TestTree;
import com.example.examplemod.test.TestWoodwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TestTree.TREE.register().addBlockTags(this::tag);
        TestWoodwork.WOODWORK1.register().addBlockTags(this::tag);
        TestWoodwork.WOODWORK2.register().addBlockTags(this::tag);
        TestChest.CHEST1.get().register().addBlockTags(this::tag);
        TestChest.TRAPPED_CHEST1.get().register().addBlockTags(this::tag);
        TestChest.CHEST2.get().register().addBlockTags(this::tag);
        TestChest.CHEST3.get().register().addBlockTags(this::tag);
    }
}
