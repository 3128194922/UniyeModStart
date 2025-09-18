package com.Uniye.Uniyesmod.Item.Impl;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MultiShotCrossbowItem extends CrossbowItem {
    public MultiShotCrossbowItem(Properties properties) {
        super(properties);
    }

    public static void performShooting(Level level, LivingEntity shooter, InteractionHand hand,
                                       ItemStack crossbow, List<ItemStack> projectiles,
                                       float velocity, float inaccuracy, boolean isCreative,
                                       @Nullable LivingEntity target) {
        for (int i = 0; i < projectiles.size(); i++) {
            ItemStack ammo = projectiles.get(i);
            if (ammo.isEmpty()) continue;

            AbstractArrow arrow = ProjectileUtil.getMobArrow(shooter, ammo, velocity);
            if (shooter instanceof Player player) {
                arrow.setCritArrow(true);
                if (isCreative || player.getAbilities().instabuild) {
                    arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }

            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(),
                    0.0F, velocity, inaccuracy);

            level.addFreshEntity(arrow);
        }

        // 播放发射音效
        level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
