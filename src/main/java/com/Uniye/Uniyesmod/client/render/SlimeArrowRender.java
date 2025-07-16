package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.SlimeArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SlimeArrowRender extends ArrowRenderer<SlimeArrow> {

    public SlimeArrowRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SlimeArrow entity) {
        return new ResourceLocation("uniyesmod", "textures/entity/slime_arrow.png");
    }
}