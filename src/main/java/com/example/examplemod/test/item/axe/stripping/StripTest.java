package com.example.examplemod.test.item.axe.stripping;

import com.example.examplemod.test.ItemGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MOD_ID;

public class StripTest {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    // MIXIN
    public static final RegistryObject<Block> BLOCK_M_BASE = BLOCKS.register("stripped_m_base", MixinA::new);
    public static final RegistryObject<Block> BLOCK_M_STRIPPED = registerBlock("stripped_m_stripped");
    // AT
    public static final RegistryObject<Block> BLOCK_A_BASE = registerBlock("stripped_a_base");
    public static final RegistryObject<Block> BLOCK_A_STRIPPED = registerBlock("stripped_a_stripped");

    public static final RegistryObject<Item> ITEM_M_BASE = registerItem(BLOCK_M_BASE);
    public static final RegistryObject<Item> ITEM_M_STRIPPED = registerItem(BLOCK_M_STRIPPED);
    public static final RegistryObject<Item> ITEM_A_BASE = registerItem(BLOCK_A_BASE);
    public static final RegistryObject<Item> ITEM_A_STRIPPED = registerItem(BLOCK_A_STRIPPED);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    private static RegistryObject<Block> registerBlock(String name) {
        return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    }

    private static RegistryObject<Item> registerItem(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemGroup.INNSTANCE)));
    }
}
