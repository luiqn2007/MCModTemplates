package com.example.examplemod.woodwork;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class ModChestBlockItem extends BlockItem {

    private final Woodwork woodwork;

    protected ModChestBlockItem(Woodwork woodwork, Block block, Properties properties) {
        super(block, properties);
        this.woodwork = woodwork;
    }

    public ModChestBlockItem(Woodwork woodwork) {
        this(woodwork, woodwork.chest(), new Properties().tab(woodwork.tab));
    }

    public Woodwork getWoodwork() {
        return woodwork;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ModChestItemRenderer.INSTANCE);
    }
}
