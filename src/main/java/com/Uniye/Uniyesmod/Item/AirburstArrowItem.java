package com.Uniye.Uniyesmod.Item;

import com.Uniye.Uniyesmod.entity.AirburstArrowEntity;
import com.Uniye.Uniyesmod.entity.ModEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AirburstArrowItem extends ArrowItem {
    public AirburstArrowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new AirburstArrowEntity(ModEntities.AIRBURST_ARROW.get(), level, shooter);
    }

}
