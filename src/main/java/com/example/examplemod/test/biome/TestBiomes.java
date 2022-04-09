package com.example.examplemod.test.biome;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TestBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, ExampleMod.MOD_ID);

    public static final RegistryObject<Biome> TEST = BIOMES.register("test_biome", () -> new Biome.BiomeBuilder()
            .temperature(100)
            .biomeCategory(Biome.BiomeCategory.DESERT)
            .downfall(1)
            .build());
}
