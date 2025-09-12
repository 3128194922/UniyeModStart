package com.Uniye.Uniyesmod.client.render.armor.render;

import com.Uniye.Uniyesmod.Item.Impl.FishingHatArmorItem;
import com.Uniye.Uniyesmod.Item.Impl.WhichArmorItem;
import com.Uniye.Uniyesmod.client.render.armor.model.FishingHatArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class FishingHatArmorRenderer extends GeoArmorRenderer<FishingHatArmorItem> {
    public FishingHatArmorRenderer() {
        super(new FishingHatArmorModel()); // 传入 GeoModel 而不是 Renderer 自身
    }
}
