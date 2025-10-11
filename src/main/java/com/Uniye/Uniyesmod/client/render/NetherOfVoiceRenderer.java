package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.NetherOfVoiceEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class NetherOfVoiceRenderer extends ThrownItemRenderer<NetherOfVoiceEntity> {

    public NetherOfVoiceRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F, false);
    }
}
