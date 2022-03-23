package com.example.examplemod.test.item.axe.stripping;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.test.RegisterUtils.*;

public class StripTest {

    public static DeferredRegister<Item> ITEMS = newItems();
    public static DeferredRegister<Block> BLOCKS = newBlocks();

    // MIXIN
    public static final RegistryObject<Block> BLOCK_M_BASE = BLOCKS.register("stripped_m_base", MixinA::new);
    public static final RegistryObject<Block> BLOCK_M_STRIPPED = defaultBlock(BLOCKS, "stripped_m_stripped");
    // AT
    public static final RegistryObject<Block> BLOCK_A_BASE = defaultBlock(BLOCKS, "stripped_a_base");
    public static final RegistryObject<Block> BLOCK_A_STRIPPED = defaultBlock(BLOCKS, "stripped_a_stripped");

    public static final RegistryObject<Item> ITEM_M_BASE = defaultItem(ITEMS, BLOCK_M_BASE);
    public static final RegistryObject<Item> ITEM_M_STRIPPED = defaultItem(ITEMS, BLOCK_M_STRIPPED);
    public static final RegistryObject<Item> ITEM_A_BASE = defaultItem(ITEMS, BLOCK_A_BASE);
    public static final RegistryObject<Item> ITEM_A_STRIPPED = defaultItem(ITEMS, BLOCK_A_STRIPPED);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
    }
}
