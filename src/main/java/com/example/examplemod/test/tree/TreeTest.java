package com.example.examplemod.test.tree;

import com.example.examplemod.test.ItemGroup;
import com.example.examplemod.tree.Tree;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Consumer;

import static com.example.examplemod.ExampleMod.MOD_ID;
import static com.example.examplemod.test.RegisterUtils.newBlocks;
import static com.example.examplemod.test.RegisterUtils.newItems;

public class TreeTest {

    public static final DeferredRegister<Item> ITEMS = newItems();
    public static final DeferredRegister<Block> BLOCKS = newBlocks();

    public static final Tree TREE = Tree.builder(MOD_ID, "test_tree")
            .tab(ItemGroup.INNSTANCE)
            .build(BLOCKS, ITEMS);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        bus.addListener((Consumer<FMLClientSetupEvent>) event -> TREE.register().registerRenderType());
    }
}
