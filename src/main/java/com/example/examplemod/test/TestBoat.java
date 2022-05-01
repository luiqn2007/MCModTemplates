package com.example.examplemod.test;

import com.example.examplemod.boat.WoodenBoat;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MOD_ID;
import static com.example.examplemod.test.Registers.*;
import static net.minecraft.world.level.block.Blocks.IRON_ORE;

public class TestBoat {

    public static final RegistryObject<WoodenBoat> BOAT = WoodenBoat.of(MOD_ID, "for_boat", TAB, () -> IRON_ORE, ITEMS, ENTITIES);

    public static void register(IEventBus bus) {
        WoodenBoat.BoatRegisterHelper.registerClient(BOAT, bus);
    }
}
