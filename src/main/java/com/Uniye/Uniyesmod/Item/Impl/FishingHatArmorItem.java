package com.Uniye.Uniyesmod.Item.Impl;

import com.Uniye.Uniyesmod.client.render.armor.render.FishingHatArmorRenderer;
import com.Uniye.Uniyesmod.client.render.armor.render.WhichArmorRenderer;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class FishingHatArmorItem  extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ImmutableMultimap<Attribute, AttributeModifier> attributeModifiers;

    // 固定UUID，保证属性唯一且稳定（4个槽位顺序：feet, legs, chest, head）
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.fromString("845DB36F-7E1F-4C29-8B7A-2F9C1A3D6E4B"), // feet
            UUID.fromString("3A8F9C2B-5D1E-4A76-9C3F-8B2E7D1A4F6C"), // legs
            UUID.fromString("7E2D4F8A-9B3C-4D1E-8A6F-3C9B2D7E1F4A"), // chest
            UUID.fromString("2B9D6E1F-4C8A-3D7E-9F2B-8A1C4D6E3F7B")  // head
    };

    private static final UUID[] TOUGHNESS_MODIFIERS = new UUID[]{
            UUID.fromString("6F3E8D2A-7C1B-4F9D-8A3E-2D6C1B9F4E8A"),
            UUID.fromString("9A2D7E1F-4B8C-3D6E-9F2A-8B1C4D7E3F6A"),
            UUID.fromString("3C8E1F6D-2A9B-4F7E-8D3C-1A6B9F2E7D4C"),
            UUID.fromString("7B4D6E2F-1A9C-3F8E-6B2D-4A7C1E9F8D3B")
    };

    public FishingHatArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();


        // 原版护甲属性（必加，否则无护甲保护）
        builder.put(Attributes.ARMOR, new AttributeModifier(
                ARMOR_MODIFIERS[type.ordinal()],
                "Armor modifier",
                this.getDefense(),
                AttributeModifier.Operation.ADDITION));

        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                TOUGHNESS_MODIFIERS[type.ordinal()],
                "Armor toughness",
                this.getToughness(),
                AttributeModifier.Operation.ADDITION));

        Attribute magicProtection = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("exmodifier", "magic_protection"));
        if (magicProtection != null) {
            builder.put(magicProtection, new AttributeModifier(
                    UUID.nameUUIDFromBytes(("magic_protection_" + type).getBytes()),
                    "Magic protection bonus",
                    0.5,
                    AttributeModifier.Operation.ADDITION));
        }

        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == this.type.getSlot()) {
            return this.attributeModifiers;
        }
        return super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new FishingHatArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original); // 准备渲染

                return this.renderer;
            }
        });
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 0, state -> {
            return PlayState.STOP; // 不播放任何动画
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}

