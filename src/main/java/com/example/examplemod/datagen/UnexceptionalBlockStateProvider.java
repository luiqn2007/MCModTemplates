package com.example.examplemod.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class UnexceptionalBlockStateProvider extends BlockStateProvider {

    private final String modid;
    private final UnexceptionalBlockModelProvider blockModels;
    private final UnexceptionalItemModelProvider itemModels;

    public UnexceptionalBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.modid = modid;
        this.blockModels = new UnexceptionalBlockModelProvider(gen, modid, exFileHelper) {
            @Override
            protected void registerModels() {
            }
        };
        this.itemModels = new UnexceptionalItemModelProvider(gen, modid, this.blockModels.existingFileHelper) {
            @Override
            protected void registerModels() {
            }
        };
    }

    @Override
    public void run(HashCache cache) throws IOException {
        super.run(cache);
        blockModels.printAllExceptions();
        itemModels.printAllExceptions();
    }

    protected abstract void registerStatesAndModels();

    public BlockModelProvider models() {
        return blockModels;
    }

    public ItemModelProvider itemModels() {
        return itemModels;
    }

    @Nonnull
    @Override
    public String getName() {
        return "BlockStates: " + modid;
    }
}
