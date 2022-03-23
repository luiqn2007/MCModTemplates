package com.example.examplemod.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.example.examplemod.ExampleMod.MOD_ID;

public class RegisterUtils {

    public static DeferredRegister<Item> newItems() {
        return DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    }

    public static DeferredRegister<Block> newBlocks() {
        return DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    }

    public static DeferredRegister<EntityType<?>> newEntities() {
        return DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    }

    public static DeferredRegister<BlockEntityType<?>> newBlockEntities() {
        return DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    }

    public static RegistryObject<Block> defaultBlock(DeferredRegister<Block> register, String name) {
        return register.register(name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    }

    public static <T extends Block> RegistryObject<Item> defaultItem(DeferredRegister<Item> register, RegistryObject<T> block) {
        return register.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ItemGroup.INNSTANCE)));
    }
}
