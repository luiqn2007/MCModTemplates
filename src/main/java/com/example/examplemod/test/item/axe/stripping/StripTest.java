package com.example.examplemod.test.item.axe.stripping;

import com.example.examplemod.test.Registers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.test.Registers.BLOCKS;
import static com.example.examplemod.test.Registers.ITEMS;

public class StripTest {

    // MIXIN
    public static final RegistryObject<Block> BLOCK_M_BASE = BLOCKS.register("stripped_m_base", MixinA::new);
    public static final RegistryObject<Block> BLOCK_M_STRIPPED = defaultBlock("stripped_m_stripped");

    public static final RegistryObject<Item> ITEM_M_BASE = defaultItem(BLOCK_M_BASE);
    public static final RegistryObject<Item> ITEM_M_STRIPPED = defaultItem(BLOCK_M_STRIPPED);

    private static RegistryObject<Block> defaultBlock(String name) {
        return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    }

    private static <T extends Block> RegistryObject<Item> defaultItem(RegistryObject<T> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(Registers.TAB)));
    }

    public static void register() {
    }
}
