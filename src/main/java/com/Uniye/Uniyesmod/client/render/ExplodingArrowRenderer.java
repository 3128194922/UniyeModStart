package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.ExplodingArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExplodingArrowRenderer extends ArrowRenderer<ExplodingArrowEntity> {
    public ExplodingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ExplodingArrowEntity entity) {
        return new ResourceLocation("uniyesmod","textures/entity/exploding_arrow.png");
    }
}
