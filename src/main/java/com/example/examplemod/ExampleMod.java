package com.example.examplemod;

import com.example.examplemod.test.command.CommandTest;
import com.example.examplemod.test.item.axe.stripping.StripTest;
import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.example.examplemod.ExampleMod.MOD_ID;

@Mod(MOD_ID)
public class ExampleMod {

    public static final String MOD_ID = "examplemod";

    public ExampleMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        CommandTest.REGISTER.register();
        StripTest.register(bus);
        TreeTest.register(bus);
        WoodworkTest.register(bus);
    }
}
