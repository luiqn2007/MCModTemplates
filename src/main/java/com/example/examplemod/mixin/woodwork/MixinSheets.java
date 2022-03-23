package com.example.examplemod.mixin.woodwork;

import com.example.examplemod.woodwork.with_mixin.Woodwork;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.renderer.Sheets.SIGN_SHEET;

@Mixin(Sheets.class)
public abstract class MixinSheets {

    /**
     * Inject in createSignMaterial, set custom tree material if tree existed
     */
    @Inject(method = "createSignMaterial", at = @At("HEAD"), cancellable = true)
    private static void injectCreateSignMaterial(WoodType woodType, CallbackInfoReturnable<Material> cir) {
        Woodwork.getWoodwork(woodType).ifPresent(woodwork -> {
            Material material = new Material(SIGN_SHEET,
                    new ResourceLocation(woodwork.name.getNamespace(), "entity/signs/" + woodwork.name.getPath()));
            cir.setReturnValue(material);
            cir.cancel();
        });
    }
}
