package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.entity.ModEntities;
import com.Uniye.Uniyesmod.entity.NetherOfVoiceEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NrtherOfVoiceItem extends ArrowItem {

    public NrtherOfVoiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new NetherOfVoiceEntity(ModEntities.NETHEROFVOICE.get(), level, shooter);
    }
}
