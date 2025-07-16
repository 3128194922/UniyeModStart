package com.Uniye.Uniyesmod.Item;

import com.Uniye.Uniyesmod.entity.ModEntities;
import com.Uniye.Uniyesmod.entity.SlimeArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SlimeArrowItem extends ArrowItem {
    public SlimeArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new SlimeArrow(ModEntities.SLIME_ARROW.get(), level, shooter);
    }
}
