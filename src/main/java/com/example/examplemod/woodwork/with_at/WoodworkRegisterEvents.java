package com.example.examplemod.woodwork.with_at;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public record WoodworkRegisterEvents(WoodworkRegister register) {

    public void onSetup(FMLCommonSetupEvent event) {
        register.registerTileEntityModifiers();
    }

    public void onClient(FMLClientSetupEvent event) {
        register.registerRender();
    }

    @OnlyIn(Dist.CLIENT)
    public Object client() {
        return new Object() {

            @SubscribeEvent
            public void onLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
                register.registerBoatLayer(event);
            }

            @SubscribeEvent
            public void onLayerRenderer(EntityRenderersEvent.RegisterRenderers event) {
                register.registerBoatRenderer(event);
            }
        };
    }
}
