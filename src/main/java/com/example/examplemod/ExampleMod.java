package com.example.examplemod;

import com.example.examplemod.test.Registers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.example.examplemod.ExampleMod.MOD_ID;

@Mod(MOD_ID)
public class ExampleMod {

    public static final String MOD_ID = "examplemod";

    public ExampleMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registers.registerAll(bus);
    }
}
