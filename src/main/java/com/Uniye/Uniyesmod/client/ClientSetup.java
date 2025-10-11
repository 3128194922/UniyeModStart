package com.Uniye.Uniyesmod.client;

import com.Uniye.Uniyesmod.client.render.*;
import com.Uniye.Uniyesmod.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "uniyesmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SLIME_ARROW.get(), SlimeArrowRender::new);
        event.registerEntityRenderer(ModEntities.NETHEROFVOICE.get(), NetherOfVoiceRenderer::new);
        event.registerEntityRenderer(ModEntities.AIRBURST_ARROW.get(), AirburstArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.EXPLODING_ARROW.get(), ExplodingArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.FINAL_EXPLODING_ARROW.get(), FinalExplodingArrowRenderer::new);
    }
}
