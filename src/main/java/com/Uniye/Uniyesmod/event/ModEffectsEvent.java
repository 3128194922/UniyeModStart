package com.Uniye.Uniyesmod.event;

import com.Uniye.Uniyesmod.Uniyesmod;
import com.Uniye.Uniyesmod.effects.ModEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Uniyesmod.MODID)
public class ModEffectsEvent {
    @SubscribeEvent
    public static void onFoodRightClick(final PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        if (player.hasEffect(ModEffects.APPETIZING.get()) && event.getItemStack().isEdible()) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemStartUse(LivingEntityUseItemEvent.Start event) {
        var ins = event.getEntity().getEffect(ModEffects.LOZENGE.get());
        if (ins != null && isEatOrDrink(event.getItem())) {
            event.setDuration(event.getDuration() >> (1 + ins.getAmplifier()));
        }
    }

    public static boolean isEatOrDrink(ItemStack stack) {
        if (stack.isEdible()) return true;
        try {
            var anim = stack.getUseAnimation();
            if (anim == UseAnim.EAT || anim == UseAnim.DRINK) return true;
        } catch (Exception ignored) {

        }
        return false;
    }
}
