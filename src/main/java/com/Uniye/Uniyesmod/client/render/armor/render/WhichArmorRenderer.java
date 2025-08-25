package com.Uniye.Uniyesmod.client.render.armor.render;

import com.Uniye.Uniyesmod.Item.Impl.WhichArmorItem;
import com.Uniye.Uniyesmod.client.render.armor.model.WhichArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WhichArmorRenderer extends GeoArmorRenderer<WhichArmorItem> {
    public WhichArmorRenderer() {
        super(new WhichArmorModel()); // 传入 GeoModel 而不是 Renderer 自身
    }
}
