package com.Uniye.Uniyesmod.Item;


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

public class WhichArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ImmutableMultimap<Attribute, AttributeModifier> attributeModifiers;

    // 固定UUID，保证属性唯一且稳定（4个槽位顺序：feet, legs, chest, head）
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.fromString("662a6b8d-da3e-4c1c-8813-96ea6097278d"), // feet
            UUID.fromString("f89d1164-86a2-4d41-99df-e94de09cac3b"), // legs
            UUID.fromString("e2b2d2b8-9e36-4f26-88fe-f62dabd5c3de"), // chest
            UUID.fromString("d8499b04-0e66-4726-ab29-64469d734e0d")  // head
    };

    private static final UUID[] TOUGHNESS_MODIFIERS = new UUID[]{
            UUID.fromString("c2a1b370-1a3d-4a9e-a222-0cf6f20b5463"),
            UUID.fromString("2dbf9e4c-4985-4530-9f4a-6314e77c420c"),
            UUID.fromString("e5aa2f40-93e3-468e-9f1c-73506a109c9d"),
            UUID.fromString("8b199ed8-3a0f-4ae5-8713-9ec9a0e2ea55")
    };

    public WhichArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
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
                    this.renderer = new WhichArmorRenderer();

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
