package com.example.examplemod.test;

import com.example.examplemod.woodwork.Woodwork;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;

import static com.example.examplemod.test.Registers.WOODWORK;

public class TestWoodwork {

    public static final Woodwork WOODWORK1 = WOODWORK.register(Woodwork.builder("woodwork1")
            .plankColor(MaterialColor.COLOR_BLUE)
            .defaultTab(Registers.TAB));

    public static final Woodwork WOODWORK2 = WOODWORK.register(Woodwork.builder("woodwork2")
            .plankColor(MaterialColor.COLOR_BLUE)
            .defaultTab(Registers.TAB));

    public static void register(IEventBus bus) {
        WOODWORK1.register().register(bus);
        WOODWORK2.register().register(bus);
    }
}
