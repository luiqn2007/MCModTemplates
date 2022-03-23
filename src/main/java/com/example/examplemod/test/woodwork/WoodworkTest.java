package com.example.examplemod.test.woodwork;

import com.example.examplemod.test.ItemGroup;
import com.example.examplemod.woodwork.with_at.Woodwork;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import static com.example.examplemod.ExampleMod.MOD_ID;
import static com.example.examplemod.test.RegisterUtils.*;

public class WoodworkTest {

    public static final DeferredRegister<Item> ITEMS = newItems();
    public static final DeferredRegister<Block> BLOCKS = newBlocks();
    public static final DeferredRegister<EntityType<?>> ENTITIES = newEntities();

    public static final Woodwork TEST_AT = Woodwork.builder(MOD_ID, "woodwork_at")
            .defaultTab(ItemGroup.INNSTANCE)
            .build(BLOCKS, ITEMS, ENTITIES);

    public static final com.example.examplemod.woodwork.with_mixin.Woodwork TEST_MIXIN = com.example.examplemod.woodwork.with_mixin.Woodwork.builder(MOD_ID, "woodwork_mixin")
            .defaultTab(ItemGroup.INNSTANCE)
            .build(BLOCKS, ITEMS);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        ENTITIES.register(bus);
        TEST_AT.register().register(bus);
        TEST_MIXIN.register().register(bus);
    }
}
