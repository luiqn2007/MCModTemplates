package com.example.examplemod.woodwork;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModChestRenderer<T extends ModChestBlockEntity> extends ChestRenderer<T> {

    private static final Map<WoodType, EnumMap<ChestType, Material>> NORMAL_MATERIALS = new HashMap<>();
    private static final Map<WoodType, EnumMap<ChestType, Material>> TRAPPED_MATERIALS = new HashMap<>();

    private final boolean xmasTextures;

    public ModChestRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
        Calendar calendar = Calendar.getInstance();
        this.xmasTextures = calendar.get(Calendar.MONTH) + 1 == 12
                && calendar.get(Calendar.DATE) >= 24
                && calendar.get(Calendar.DATE) <= 26;
    }

    @Override
    protected Material getMaterial(T blockEntity, ChestType chestType) {
        if (xmasTextures) {
            return super.getMaterial(blockEntity, chestType);
        }
        return chooseMaterial(blockEntity instanceof ModChestTrappedBlockEntity, blockEntity.getWoodType(), chestType);
    }

    private Material chooseMaterial(boolean trapped, WoodType wood, ChestType type) {
        return (trapped ? TRAPPED_MATERIALS : NORMAL_MATERIALS).computeIfAbsent(wood, __ -> {
            EnumMap<ChestType, Material> map = new EnumMap<>(ChestType.class);
            switch (type) {
                case LEFT -> map.put(type, chestMaterial(wood, "left", trapped));
                case RIGHT -> map.put(type, chestMaterial(wood, "right", trapped));
                case SINGLE -> map.put(type, chestMaterial(wood, null, trapped));
            }
            return map;
        }).get(type);
    }

    private Material chestMaterial(WoodType wood, @Nullable String part, boolean trapped) {
        if (wood == WoodType.OAK) {
            return new Material(Sheets.CHEST_SHEET, new ResourceLocation("entity/chest/" + part));
        } else {
            ResourceLocation rl = new ResourceLocation(wood.name());
            StringBuilder rName = new StringBuilder(rl.getPath());
            if (trapped) rName.append("_trapped");
            if (part != null) rName.append('_').append(part);
            return new Material(Sheets.CHEST_SHEET, new ResourceLocation(rl.getNamespace(), "entity/chest/" + rName));
        }
    }
}
