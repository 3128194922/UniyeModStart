package com.Uniye.Uniyesmod.event;

import com.Uniye.Uniyesmod.Item.GravityCoreItem;
import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Uniyesmod.MODID)
public class LivingFallHandler {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (GravityCoreItem.isWearingGravityCore(player)){
            event.setDamageMultiplier(0);
            return;
        }
    }
}
