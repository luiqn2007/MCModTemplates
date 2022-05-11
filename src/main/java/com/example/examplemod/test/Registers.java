package com.example.examplemod.test;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.command.CommandRegister;
import com.example.examplemod.woodwork.NetworkHolder;
import com.example.examplemod.woodwork.WoodworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.examplemod.ExampleMod.MOD_ID;

public class Registers {

    public static final ModTab TAB = new ModTab();

    public static final AtomicInteger NETWORK_ID = new AtomicInteger(0);
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "channel0"), () -> "1", s -> true, s -> true);
    public static final NetworkHolder NW_HOLDER = NetworkHolder.simple(NETWORK, NETWORK_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, ExampleMod.MOD_ID);

    public static final CommandRegister COMMANDS = new CommandRegister();
    public static final WoodworkManager WOODWORK = new WoodworkManager(MOD_ID, ITEMS, BLOCKS, BLOCK_ENTITIES, ENTITIES, NW_HOLDER);

    public static void registerAll(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ENTITIES.register(bus);
        BIOMES.register(bus);
        COMMANDS.register();

        TestCommand.register();
        TestTree.register(bus);
        TestWoodwork.register(bus);
        TestChest.register(bus);
        TestBoat.register(bus);
    }
}
