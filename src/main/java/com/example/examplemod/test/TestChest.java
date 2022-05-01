package com.example.examplemod.test;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.chest.WoodenChest;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

import static com.example.examplemod.test.Registers.*;
import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraft.world.level.material.MaterialColor.*;

public class TestChest {

    public static final RegistryObject<WoodenChest> CHEST1 = WoodenChest.of(ExampleMod.MOD_ID, "chest1", COLOR_BLUE, TAB, () -> REDSTONE_BLOCK, false, BLOCKS, ITEMS, BLOCK_ENTITIES);
    public static final RegistryObject<WoodenChest> TRAPPED_CHEST1 = WoodenChest.of(ExampleMod.MOD_ID, "chest1", COLOR_LIGHT_BLUE, TAB, () -> REDSTONE_BLOCK, true, BLOCKS, ITEMS, BLOCK_ENTITIES);
    public static final RegistryObject<WoodenChest> CHEST2 = WoodenChest.of(ExampleMod.MOD_ID, "chest2", COLOR_GRAY, TAB, () -> LAPIS_BLOCK, false, BLOCKS, ITEMS, BLOCK_ENTITIES);
    public static final RegistryObject<WoodenChest> CHEST3 = WoodenChest.of(ExampleMod.MOD_ID, "chest3", COLOR_BLACK, TAB, () -> ACACIA_LOG, true, BLOCKS, ITEMS, BLOCK_ENTITIES);

    public static void register(IEventBus bus) {
        bus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> CHEST1.get().register().registerRenderer(event));
        bus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> TRAPPED_CHEST1.get().register().registerRenderer(event));
        bus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> CHEST2.get().register().registerRenderer(event));
        bus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> CHEST3.get().register().registerRenderer(event));
    }
}
