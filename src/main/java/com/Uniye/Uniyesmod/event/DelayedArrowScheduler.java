package com.Uniye.Uniyesmod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber
public class DelayedArrowScheduler {

    private static final List<ArrowTask> TASKS = new LinkedList<>();

    public static void schedule(ServerLevel level, LivingEntity shooter, ItemStack bow, float power, int delayTicks) {
        TASKS.add(new ArrowTask(level, shooter, bow.copy(), power, delayTicks));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<ArrowTask> it = TASKS.iterator();
        while (it.hasNext()) {
            ArrowTask task = it.next();
            task.delay--;

            if (task.delay <= 0) {
                shootArrow(task.level, task.shooter, task.bow, task.power);
                it.remove();
            }
        }
    }

    private static void shootArrow(ServerLevel level, LivingEntity shooter, ItemStack bow, float power) {
        ItemStack ammo = new ItemStack(Items.ARROW);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(shooter, ammo, power);
        if (arrow == null) return;

        if (bow.getItem() instanceof BowItem bowItem) {
            arrow = bowItem.customArrow(arrow);
        }

        arrow.pickup = AbstractArrow.Pickup.ALLOWED;

        arrow.shootFromRotation(
                shooter,
                shooter.getXRot(),
                shooter.getYRot(),
                0.0F,
                power * 3.0F,
                1.0F
        );

        level.addFreshEntity(arrow);
    }

    private static class ArrowTask {
        final ServerLevel level;
        final LivingEntity shooter;
        final ItemStack bow;
        final float power;
        int delay;

        ArrowTask(ServerLevel level, LivingEntity shooter, ItemStack bow, float power, int delay) {
            this.level = level;
            this.shooter = shooter;
            this.bow = bow;
            this.power = power;
            this.delay = delay;
        }
    }
}