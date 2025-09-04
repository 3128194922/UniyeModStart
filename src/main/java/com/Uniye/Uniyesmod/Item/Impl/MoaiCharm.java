package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.Uniyesmod;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.Uniye.Uniyesmod.Item.ModItems;

import java.util.UUID;

public class MoaiCharm extends Item implements ICurioItem {

    public MoaiCharm(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return true;
    }

    public static boolean isWearingMoaiCharm(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity)
                .map(handler -> !handler.findCurios(ModItems.MOAI_CHARM.get()).isEmpty())
                .orElse(false);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("9b3ef174-e8eb-4ba7-a287-4575bdb540d0"), Uniyesmod.MODID+":moai_charm_knockback_resistance_bonus", 1, AttributeModifier.Operation.ADDITION));
        return attributes;
    }

}