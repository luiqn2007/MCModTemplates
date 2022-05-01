package com.example.examplemod.test.datagen;

import com.example.examplemod.test.TestBoat;
import com.example.examplemod.test.TestChest;
import com.example.examplemod.test.TestWoodwork;
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

        TestBoat.BOAT.get().register().verifyTexture(helper);
        TestChest.CHEST1.get().register().verifyTexture(helper);
        TestChest.TRAPPED_CHEST1.get().register().verifyTexture(helper);
        TestChest.CHEST2.get().register().verifyTexture(helper);
        TestChest.CHEST3.get().register().verifyTexture(helper);
        TestWoodwork.WOODWORK1.register().verifyTexture(helper);
        TestWoodwork.WOODWORK2.register().verifyTexture(helper);

        BlockTagsProvider bp;
        generator.addProvider(new LanguageProvider(generator, MOD_ID));
        generator.addProvider(new BlockStateProvider(generator, MOD_ID, helper));
        generator.addProvider(new ItemModelProvider(generator, MOD_ID, helper));
        generator.addProvider(new RecipeProvider(generator));
        generator.addProvider(bp = new BlockTagProvider(generator, MOD_ID, helper));
        generator.addProvider(new ItemTagProvider(generator, bp, MOD_ID, helper));
        generator.addProvider(new BlockLootProvider(generator));
        // generator.addProvider(new WorldProvider(generator, MOD_ID));
    }
}
