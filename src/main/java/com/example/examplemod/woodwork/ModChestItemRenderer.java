package com.example.examplemod.woodwork;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModChestItemRenderer extends BlockEntityWithoutLevelRenderer implements IItemRenderProperties {

    public static final ModChestItemRenderer INSTANCE = new ModChestItemRenderer();

    private final Map<WoodType, ModChestTrappedBlockEntity> trappedChests = new HashMap<>();
    private final Map<WoodType, ModChestBlockEntity> chests = new HashMap<>();

    public ModChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        return this;
    }

    @Override
    public void renderByItem(ItemStack item, ItemTransforms.TransformType transformType, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        ModChestBlockEntity entity;
        if (item.getItem() instanceof ModChestTrappedBlockItem trappedChest) {
            Woodwork woodwork = trappedChest.getWoodwork();
            entity = trappedChests.computeIfAbsent(woodwork.type, __ -> new ModChestTrappedBlockEntity(BlockPos.ZERO, woodwork.trappedChest().defaultBlockState()));
        } else if (item.getItem() instanceof ModChestBlockItem chest) {
            Woodwork woodwork = chest.getWoodwork();
            entity = chests.computeIfAbsent(woodwork.type, __ -> new ModChestBlockEntity(BlockPos.ZERO, woodwork.chest().defaultBlockState()));
        } else {
            return;
        }

        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(entity, stack, buffer, light, overlay);
    }
}
