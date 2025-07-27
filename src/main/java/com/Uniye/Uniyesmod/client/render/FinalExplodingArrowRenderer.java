package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.FinalExplodingArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FinalExplodingArrowRenderer extends ArrowRenderer<FinalExplodingArrowEntity> {
    public FinalExplodingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FinalExplodingArrowEntity entity) {
        return new ResourceLocation("uniyesmod", "textures/entity/final_exploding_arrow.png");
    }
}
