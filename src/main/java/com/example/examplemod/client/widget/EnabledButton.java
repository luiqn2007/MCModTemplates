package com.example.examplemod.client.widget;

import com.example.examplemod.client.GLSwitcher;
import com.example.examplemod.client.TextureAtlas;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class EnabledButton extends Button {

    private final String name;
    private final TextureAtlas atlas;
    private boolean isEnable = false;

    public EnabledButton(int pWidth, int pHeight, TextureAtlas alias, String name, String hoverKey, Consumer<EnabledButton> pOnPress) {
        super(0, 0, pWidth, pHeight, new TranslatableComponent(hoverKey), pButton -> {
            if (pButton.active) {
                pOnPress.accept((EnabledButton) pButton);
            }
        });
        this.name = name;
        this.atlas = alias;
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        String n = isEnable() ? isHoveredOrFocused() ? name + "_hover" : name : name + "_disable";
        atlas.blit(pPoseStack, n, x, y, width, height, GLSwitcher.blend().enable(), GLSwitcher.depth().enable());
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public EnabledButton resize(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
