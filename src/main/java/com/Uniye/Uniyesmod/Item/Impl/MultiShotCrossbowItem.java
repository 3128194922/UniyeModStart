package com.Uniye.Uniyesmod.Item.Impl;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class MultiShotCrossbowItem extends CrossbowItem {

    public MultiShotCrossbowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!isCharged(stack)) return;

        shootMultiArrows(serverLevel, shooter, stack, 3, 10.0F);

        // 发射后重置上弹状态
        setCharged(stack, false);
        if (shooter instanceof Player player) {
            clearChargedProjectiles(stack);
        }
    }

    private void shootMultiArrows(ServerLevel level, LivingEntity shooter, ItemStack crossbow, int arrowCount, float spreadDeg) {
        ListTag projectiles = crossbow.getOrCreateTag().getList("ChargedProjectiles", 10);
        if (projectiles.isEmpty()) return;

        int count = Math.min(arrowCount, projectiles.size());

        for (int i = 0; i < count; i++) {
            CompoundTag arrowTag = projectiles.getCompound(i);
            ItemStack ammo = ItemStack.of(arrowTag);

            AbstractArrow arrow = ProjectileUtil.getMobArrow(shooter, ammo, 1.0F);
            if (arrow == null) continue;

            applyCrossbowEnchantments(arrow, crossbow);

            float yawOffset = (i - (count - 1) / 2.0F) * spreadDeg;
            arrow.shootFromRotation(shooter,
                    shooter.getXRot(),
                    shooter.getYRot() + yawOffset,
                    0.0F,
                    3.15F,
                    1.0F);

            level.addFreshEntity(arrow);
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
        if (!(entity instanceof Player player) || level.isClientSide) return;

        if (!isCharged(stack)) {
            boolean isCreative = player.getAbilities().instabuild;

            // 非创造模式，检查是否至少有 3 支箭
            if (!isCreative && countArrows(player) < 3) return;

            // 消耗 3 支箭（创造模式不消耗）
            ItemStack[] consumed = new ItemStack[3];
            if (!isCreative) {
                for (int i = 0; i < 3; i++) {
                    consumed[i] = consumeArrow(player);
                    if (consumed[i].isEmpty()) return; // 意外不足时阻止上弹
                }
            } else {
                // 创造模式使用空箭堆作为占位
                for (int i = 0; i < 3; i++) {
                    consumed[i] = new ItemStack(Items.ARROW);
                }
            }

            // 标记上弹
            setCharged(stack, true);
            for (ItemStack ammo : consumed) {
                addChargedProjectile(stack, ammo.copy());
            }
        }
    }

    /** 统计玩家箭的数量 */
    private int countArrows(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == Items.ARROW) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /** 从玩家身上消耗一支箭，返回消耗的箭堆 */
    private ItemStack consumeArrow(Player player) {
        // 主手
        ItemStack main = player.getMainHandItem();
        if (main.getItem() == Items.ARROW) {
            ItemStack copy = main.copy();
            main.shrink(1);
            return copy;
        }

        // 副手
        ItemStack off = player.getOffhandItem();
        if (off.getItem() == Items.ARROW) {
            ItemStack copy = off.copy();
            off.shrink(1);
            return copy;
        }

        // 背包
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot.getItem() == Items.ARROW) {
                ItemStack copy = slot.copy();
                slot.shrink(1);
                return copy;
            }
        }

        return ItemStack.EMPTY;
    }

    private void addChargedProjectile(ItemStack stack, ItemStack projectile) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.getList("ChargedProjectiles", 10);
        list.add(projectile.save(new CompoundTag()));
        tag.put("ChargedProjectiles", list);
    }

    private void clearChargedProjectiles(ItemStack stack) {
        stack.getOrCreateTag().remove("ChargedProjectiles");
    }
}