package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.event.DelayedArrowScheduler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BurstBowItem extends BowItem {

    public BurstBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player player)) return;
        if (level.isClientSide) return;

        int useDuration = this.getUseDuration(stack) - timeLeft;
        float power = getPowerForTime(useDuration);
        if (power < 0.1F) return; // 拉弓不足

        // 创造模式不消耗箭
        boolean isCreative = player.getAbilities().instabuild;

        // 生存模式检查库存是否至少有3支箭
        if (!isCreative && countArrows(player) < 3) return;

        // 三连发：间隔 4 tick
        for (int i = 0; i < 3; i++) {
            int delay = i * 4;
            DelayedArrowScheduler.schedule((ServerLevel) level, shooter, stack.copy(), power, delay);
        }

        // 消耗箭：每发消耗一支，总共 3 支（创造模式不消耗）
        if (!isCreative) {
            for (int i = 0; i < 3; i++) {
                consumeArrow(player);
            }
        }

        // 耐久损耗
        stack.hurtAndBreak(1, shooter, p -> p.broadcastBreakEvent(shooter.getUsedItemHand()));
    }

    /** 统计玩家箭的数量 */
    private int countArrows(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof ArrowItem) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /** 从玩家身上消耗一支箭 */
    private void consumeArrow(Player player) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.getItem() instanceof ArrowItem) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                return;
            }
        }
    }
}