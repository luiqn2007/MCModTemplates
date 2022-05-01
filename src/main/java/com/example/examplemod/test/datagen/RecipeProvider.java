package com.example.examplemod.test.datagen;

import com.example.examplemod.test.TestBoat;
import com.example.examplemod.test.TestChest;
import com.example.examplemod.test.TestTree;
import com.example.examplemod.test.TestWoodwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        TestTree.TREE.register().addRecipes(consumer, Blocks.COAL_ORE);
        TestWoodwork.WOODWORK1.register().addRecipes(consumer, Blocks.COPPER_ORE);
        TestWoodwork.WOODWORK2.register().addRecipes(consumer, Blocks.IRON_ORE);
        TestChest.CHEST1.get().register().addRecipes(consumer);
        TestChest.TRAPPED_CHEST1.get().register().addTrappedRecipes(consumer, Blocks.SAND);
        TestChest.CHEST2.get().register().addRecipes(consumer);
        TestChest.CHEST3.get().register().addTrappedRecipes(consumer, Blocks.COAL_ORE);

        TestBoat.BOAT.get().register().addRecipes(consumer);
    }
}
