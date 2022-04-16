package com.example.examplemod.test.datagen;

import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
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
        TreeTest.TREE.register().addBlockTags(this::tag);
        WoodworkTest.WOODWORK_WITH_CHEST.register().addBlockTags(this::tag);
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addBlockTags(this::tag);
    }
}
