package com.Uniye.Uniyesmod.Item;

import com.Uniye.Uniyesmod.entity.ExplodingArrowEntity;
import com.Uniye.Uniyesmod.entity.ModEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ExplodingArrowItem extends ArrowItem {
    public ExplodingArrowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new ExplodingArrowEntity(ModEntities.EXPLODING_ARROW.get(), level, shooter);
    }
}
