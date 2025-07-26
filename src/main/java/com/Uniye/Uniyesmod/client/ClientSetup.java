package com.Uniye.Uniyesmod.client;

import com.Uniye.Uniyesmod.client.render.SlimeArrowRender;
import com.Uniye.Uniyesmod.client.render.SeekingArrowRenderer;
import com.Uniye.Uniyesmod.entity.ModEntities;
import com.Uniye.Uniyesmod.entity.SeekingArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "uniyesmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SLIME_ARROW.get(), SlimeArrowRender::new);
        event.registerEntityRenderer(ModEntities.SEEKING_ARROW.get(), SeekingArrowRenderer::new);
    }
}
