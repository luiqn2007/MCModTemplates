package com.example.examplemod.block.chest;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WoodenChestItem extends BlockItem {

    private static final ThreadLocal<WoodenChest> chestHolder = new ThreadLocal<>();

    public static WoodenChestItem create(WoodenChest chest, CreativeModeTab tab) {
        chestHolder.set(chest);
        WoodenChestItem item = new WoodenChestItem(tab);
        chestHolder.remove();
        return item;
    }

    private WoodenChestItem(CreativeModeTab tab) {
        super(chestHolder.get(), new Properties().tab(tab));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 300;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        WoodenChest chest = chestHolder.get();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> consumer.accept(chest.renderer()));
    }
}
