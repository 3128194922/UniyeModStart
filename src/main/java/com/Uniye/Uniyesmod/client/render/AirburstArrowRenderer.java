package com.Uniye.Uniyesmod.client.render;

import com.Uniye.Uniyesmod.entity.AirburstArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AirburstArrowRenderer extends ArrowRenderer<AirburstArrowEntity> {
    public AirburstArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AirburstArrowEntity entity) {
        // 使用原版箭矢贴图
        return new ResourceLocation("uniyesmod","textures/entity/airburst_arrow.png");
    }
}
