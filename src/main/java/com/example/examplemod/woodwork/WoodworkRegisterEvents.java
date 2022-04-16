package com.example.examplemod.woodwork;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public record WoodworkRegisterEvents(WoodworkRegisterHelper register) {

    public void onSetup(FMLCommonSetupEvent event) {
    }

    public void onClient(FMLClientSetupEvent event) {
        register.registerRender();
    }

    @OnlyIn(Dist.CLIENT)
    public Object client() {
        return new Object() {

            @SubscribeEvent
            public void onLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
                register.registerLayers(event);
            }

            @SubscribeEvent
            public void onLayerRenderer(EntityRenderersEvent.RegisterRenderers event) {
                register.registerRenderer(event);
            }
        };
    }

    public Object forge() {
        return new Object() {

            @SubscribeEvent
            public void onFuel(FurnaceFuelBurnTimeEvent event) {
                register.registerFuel(event);
            }
        };
    }
}
