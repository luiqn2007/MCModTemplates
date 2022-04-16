package com.example.examplemod.test.woodwork;

import com.example.examplemod.test.ModTab;
import com.example.examplemod.woodwork.NetworkHolder;
import com.example.examplemod.woodwork.Woodwork;
import com.example.examplemod.woodwork.WoodworkManager;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;

import static com.example.examplemod.ExampleMod.MOD_ID;
import static com.example.examplemod.test.Registers.*;
import static com.example.examplemod.woodwork.Woodwork.builder;

public class WoodworkTest {

    public static final NetworkHolder NW_HOLDER = NetworkHolder.simple(NETWORK, NETWORK_ID);
    public static final boolean REG_FINISHED = WoodworkManager.initialize(ITEMS, BLOCKS, BLOCK_ENTITY_TYPES, ENTITIES, NW_HOLDER);

    public static final Woodwork WOODWORK_WITH_CHEST = WoodworkManager.register(builder(MOD_ID, "woodwork1")
            .plankColor(MaterialColor.COLOR_BLUE)
            .defaultTab(ModTab.INNSTANCE));

    public static final Woodwork WOODWORK_WITHOUT_CHEST = WoodworkManager.register(builder(MOD_ID, "woodwork2")
            .plankColor(MaterialColor.COLOR_BLUE)
            .defaultTab(ModTab.INNSTANCE)
            .noChest());

    public static void register(IEventBus bus) {
        WOODWORK_WITH_CHEST.register().register(bus);
        WOODWORK_WITHOUT_CHEST.register().register(bus);
    }
}
