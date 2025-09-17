package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.event.DelayedArrowScheduler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BurstBowItem extends BowItem {

    public BurstBowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player)) return;
        if (level.isClientSide) return;

        int useDuration = this.getUseDuration(stack) - timeLeft;
        float power = getPowerForTime(useDuration);
        if (power < 0.1F) return; // 拉弓不足

        // 三连发：间隔 4 tick
        for (int i = 0; i < 3; i++) {
            int delay = i * 4;
            DelayedArrowScheduler.schedule((ServerLevel) level, shooter, stack.copy(), power, delay);
        }

        stack.hurtAndBreak(1, shooter, p -> p.broadcastBreakEvent(shooter.getUsedItemHand()));
    }
}