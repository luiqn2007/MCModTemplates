package com.example.examplemod.test.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.example.examplemod.ExampleMod.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class _GenEventHandler {

    @SubscribeEvent
    public static void onGather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        BlockTagsProvider bp;

        generator.addProvider(new LanguageProvider(generator, MOD_ID));
        generator.addProvider(new BlockStateProvider(generator, MOD_ID, helper));
        generator.addProvider(new ItemModelProvider(generator, MOD_ID, helper));
        generator.addProvider(new RecipeProvider(generator));
        generator.addProvider(bp = new BlockTagProvider(generator, MOD_ID, helper));
        generator.addProvider(new ItemTagProvider(generator, bp, MOD_ID, helper));
        generator.addProvider(new BlockLootProvider(generator));
        generator.addProvider(new WorldProvider(generator, MOD_ID));
    }
}
