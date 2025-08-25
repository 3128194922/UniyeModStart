package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.Config;
import com.Uniye.Uniyesmod.Item.ModItems;
import com.Uniye.Uniyesmod.Utils.Scheduler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class MoonItem extends Item {

    private static final UUID GRAVITY_MODIFIER_UUID = UUID.fromString("1fa5c3f2-4e37-44e2-a618-d747870fbd05");

    public MoonItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            boolean isSneaking = player.isShiftKeyDown();
            boolean isInMainHand = player.getMainHandItem().is(ModItems.MOON.get());
            AttributeInstance gravityAttribute = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get());

            if (gravityAttribute != null) {
                AttributeModifier existingModifier = gravityAttribute.getModifier(GRAVITY_MODIFIER_UUID);

                if (isInMainHand) {
                    if (!isSneaking) {
                        if (existingModifier == null) {
                            gravityAttribute.addTransientModifier(new AttributeModifier(
                                    GRAVITY_MODIFIER_UUID,
                                    "Moon gravity reduction",
                                    Config.MoonGravityReduction,
                                    AttributeModifier.Operation.ADDITION
                            ));
                        }
                    } else {
                        if (existingModifier != null) {
                            gravityAttribute.removeModifier(GRAVITY_MODIFIER_UUID);
                        }
                    }
                }
            }

            Scheduler.schedule(() -> {
                boolean stillHolding = player.getMainHandItem().is(ModItems.MOON.get());
                AttributeInstance gravityAttributeLater = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get());

                if (!stillHolding && gravityAttributeLater != null) {
                    AttributeModifier modifier = gravityAttributeLater.getModifier(GRAVITY_MODIFIER_UUID);
                    if (modifier != null) {
                        gravityAttributeLater.removeModifier(GRAVITY_MODIFIER_UUID);
                    }
                }
            }, 4);
        }
    }
}
