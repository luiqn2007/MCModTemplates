package com.example.examplemod.test.datagen;

import com.example.examplemod.test.tree.TreeTest;
import com.example.examplemod.test.woodwork.WoodworkTest;
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
        TreeTest.TREE.register().addRecipes(consumer, Blocks.COAL_ORE);
        WoodworkTest.WOODWORK_WITH_CHEST.register().addRecipes(consumer, Blocks.COPPER_ORE);
        WoodworkTest.WOODWORK_WITHOUT_CHEST.register().addRecipes(consumer, Blocks.IRON_ORE);
    }
}
