package com.example.examplemod.test;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemGroup extends CreativeModeTab {

    public static final ItemGroup INNSTANCE = new ItemGroup();

    public ItemGroup() {
        super(ExampleMod.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.MUSIC_DISC_11);
    }
}
