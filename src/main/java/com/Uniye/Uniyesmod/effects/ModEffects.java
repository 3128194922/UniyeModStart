package com.Uniye.Uniyesmod.effects;

import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    private ModEffects() {
    }

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Uniyesmod.MODID);

    public static final RegistryObject<MobEffect> APPETIZING = EFFECTS.register("appetizing", () -> new RegisterEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final RegistryObject<MobEffect> LOZENGE = EFFECTS.register("lozenge", () -> new RegisterEffect(MobEffectCategory.BENEFICIAL, 0x650808));
}
