package com.Uniye.Uniyesmod.Item.Impl;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class MultiShotCrossbowItem extends CrossbowItem {

    public MultiShotCrossbowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!isCharged(stack)) return; // 只有上弹状态才发射

        shootMultiArrows(serverLevel, shooter, stack, 3, 10.0F);

        // 发射后重置上弹状态
        setCharged(stack, false);
        if (shooter instanceof Player player) {
            clearChargedProjectiles(stack);
        }
    }

    private void shootMultiArrows(ServerLevel level, LivingEntity shooter, ItemStack crossbow, int arrowCount, float spreadDeg) {
        boolean hasInfinity = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbow) > 0;

        for (int i = 0; i < arrowCount; i++) {
            ItemStack ammo = findAmmo(shooter);
            if (ammo.isEmpty() && !hasInfinity) return; // 没箭矢且没有无限附魔就停

            AbstractArrow arrow = ProjectileUtil.getMobArrow(shooter, ammo, 1.0F);
            if (arrow == null) continue;

            applyCrossbowEnchantments(arrow, crossbow);

            float yawOffset = (i - (arrowCount - 1) / 2.0F) * spreadDeg;
            arrow.shootFromRotation(shooter,
                    shooter.getXRot(),
                    shooter.getYRot() + yawOffset,
                    0.0F,
                    3.15F,
                    1.0F);

            level.addFreshEntity(arrow);

            // 消耗箭矢（如果玩家不是创造模式且没有无限附魔）
            if (shooter instanceof Player player && !player.getAbilities().instabuild && !hasInfinity) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.getInventory().removeItem(ammo);
                }
            }
        }
    }

    private void applyCrossbowEnchantments(AbstractArrow arrow, ItemStack crossbow) {
        int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, crossbow);
        if (power > 0) {
            arrow.setBaseDamage(arrow.getBaseDamage() + (double) power * 0.5 + 0.5);
        }

        int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, crossbow);
        if (punch > 0) {
            arrow.setKnockback(punch);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, crossbow) > 0) {
            arrow.setSecondsOnFire(100);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        // 装填箭矢时使用
        if (!(entity instanceof Player player) || level.isClientSide) return;

        if (!isCharged(stack)) {
            ItemStack ammo = findAmmo(entity);
            if (!ammo.isEmpty()) {
                setCharged(stack, true);
                addChargedProjectile(stack, ammo.copy());
                // 不消耗箭矢，留到发射时消耗
            }
        }
    }

    private void addChargedProjectile(ItemStack stack, ItemStack projectile) {
        ListTag list = stack.getOrCreateTag().getList("ChargedProjectiles", 10);
        list.add(projectile.save(new CompoundTag()));
        stack.getOrCreateTag().put("ChargedProjectiles", list);
    }

    private void clearChargedProjectiles(ItemStack stack) {
        stack.getOrCreateTag().remove("ChargedProjectiles");
    }

    private ItemStack findAmmo(LivingEntity shooter) {
        if (shooter instanceof Player player) {
            // 优先取手持箭
            ItemStack main = player.getMainHandItem();
            if (main.is(Items.ARROW)) return main;

            ItemStack off = player.getOffhandItem();
            if (off.is(Items.ARROW)) return off;

            // 遍历背包
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack slot = player.getInventory().getItem(i);
                if (slot.is(Items.ARROW)) return slot;
            }
        }
        return new ItemStack(Items.ARROW);
    }
}