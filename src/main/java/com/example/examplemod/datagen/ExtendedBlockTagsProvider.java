package com.example.examplemod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExtendedBlockTagsProvider extends BlockTagsProvider {

    public ExtendedBlockTagsProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    public void addPickaxe(Block... blocks) {
        add(BlockTags.MINEABLE_WITH_PICKAXE, blocks);
    }

    public void addPickaxe(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.MINEABLE_WITH_PICKAXE, blocks);
    }

    public void addAxe(Block... blocks) {
        add(BlockTags.MINEABLE_WITH_AXE, blocks);
    }

    public void addAxe(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.MINEABLE_WITH_AXE, blocks);
    }

    public void addShovel(Block... blocks) {
        add(BlockTags.MINEABLE_WITH_SHOVEL, blocks);
    }

    public void addShovel(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.MINEABLE_WITH_SHOVEL, blocks);
    }

    public void addHoe(Block... blocks) {
        add(BlockTags.MINEABLE_WITH_HOE, blocks);
    }

    public void addHoe(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.MINEABLE_WITH_HOE, blocks);
    }

    public void addStoneTool(Block... blocks) {
        add(BlockTags.NEEDS_STONE_TOOL, blocks);
    }

    public void addStoneTool(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.NEEDS_STONE_TOOL, blocks);
    }

    public void addIronTool(Block... blocks) {
        add(BlockTags.NEEDS_IRON_TOOL, blocks);
    }

    public void addIronTool(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.NEEDS_IRON_TOOL, blocks);
    }

    public void addDiamondTool(Block... blocks) {
        add(BlockTags.NEEDS_DIAMOND_TOOL, blocks);
    }

    public void addDiamondTool(RegistryObject<? extends Block>... blocks) {
        add(BlockTags.NEEDS_DIAMOND_TOOL, blocks);
    }

    public void add(TagKey<Block> tag, Block... blocks) {
        tag(tag).add(blocks);
    }

    public void add(TagKey<Block> tag, RegistryObject<? extends Block>... blocks) {
        tag(tag).add(Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new));
    }

    public void add(TagKey<Block> tag, Collection<? extends Block> blocks) {
        tag(tag).add(blocks.toArray(Block[]::new));
    }

    public void add(TagKey<Block> tag, Stream<? extends Block> blocks) {
        tag(tag).add(blocks.toArray(Block[]::new));
    }
}
