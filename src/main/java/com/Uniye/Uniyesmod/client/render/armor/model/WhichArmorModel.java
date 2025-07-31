package com.Uniye.Uniyesmod.client.render.armor.model;

import com.Uniye.Uniyesmod.Item.WhichArmorItem;
import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WhichArmorModel extends GeoModel<WhichArmorItem> {
    @Override
    public ResourceLocation getModelResource(WhichArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "geo/whicharmor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WhichArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "textures/armor/which_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WhichArmorItem animatable) {
        return new ResourceLocation(Uniyesmod.MODID, "animations/which_armor.animation.json");
    }

}
