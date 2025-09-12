package com.Uniye.Uniyesmod.client.render.armor.model;

import com.Uniye.Uniyesmod.Item.Impl.FishingHatArmorItem;
import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FishingHatArmorModel extends GeoModel<FishingHatArmorItem> {
    @Override
    public ResourceLocation getModelResource(FishingHatArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "geo/fishing_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FishingHatArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "textures/armor/fishing_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FishingHatArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "animations/fishing_hat.animation.json");
    }
}
