package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.SeekingArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SeekingArrowRenderer extends ArrowRenderer<SeekingArrowEntity> {

    public SeekingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SeekingArrowEntity entity) {
        return new ResourceLocation("uniyesmod", "textures/entity/seeking_arrow.png");
    }
}
