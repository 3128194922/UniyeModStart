package com.Uniye.Uniyesmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Uniyesmod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
/*
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
*/
    private static final ForgeConfigSpec.ConfigValue<Double> GRAVITY_CORE_GRAVITY_MULTIPLIER = BUILDER
            .comment("Multiplier applied to gravity when holding SHIFT with Gravity Core.")
            .defineInRange("gravityCoreGravityMultiplier", 5.0, 0.0, 100.0);
    private static final ForgeConfigSpec.ConfigValue<Double> MOON_GRAVITY_REDUCTION  = BUILDER
            .comment("Gravity change applied while holding the Moon item.\n" +
                    "Default Minecraft gravity is 0.08.")
            .defineInRange("moonGravityReduction", -0.1, -1.0, 1.0);
    private static final ForgeConfigSpec.ConfigValue<Double> GRAVITY_CORE_IMPACT_DAMAGE_COEFFICIENT = BUILDER
            .comment("Impact damage coefficient when falling with Gravity Core.\n" +
                    "Damage = player's attack damage * fall speed * this value.")
            .defineInRange("gravityCoreImpactDamageCoefficient", 0.5, 0.0, 10.0);
    private static final ForgeConfigSpec.ConfigValue<Double> GRAVITY_CORE_IMPACT_RADIUS_COEFFICIENT = BUILDER
            .comment("Impact radius coefficient when falling with Gravity Core.\n" +
                    "Impact radius = fall speed * this value.")
            .defineInRange("gravityCoreImpactRadiusCoefficient", 1.0, 0.0, 10.0);
    private static final ForgeConfigSpec.IntValue GRAVITY_CORE_COOLDOWN = BUILDER
            .comment("Cooldown in ticks for Gravity Core jump (20 ticks = 1 second).")
            .defineInRange("gravityCoreCooldown", 0, 0, 100000);
    private static final ForgeConfigSpec.ConfigValue<Double> GRAVITY_CORE_JUMP_STRENGTH = BUILDER
            .comment("Jump strength boost applied when using Gravity Core.")
            .defineInRange("gravityCoreJumpStrength", 5.0, 0.0, 50.0);
    private static final ForgeConfigSpec.ConfigValue<Double> GIANTS_RING_ATTACK_DAMAGE = BUILDER
            .comment("Bonus attack damage granted by Giant's Ring.")
            .defineInRange("giantsRingAttackDamage", 3.0, 0.0, 1024.0);
    private static final ForgeConfigSpec.ConfigValue<Double> GIANTS_RING_MAX_HEALTH = BUILDER
            .comment("Bonus max health granted by Giant's Ring.")
            .defineInRange("giantsRingMaxHealth", 10.0, 0.0, 1024.0);
    private static final ForgeConfigSpec.ConfigValue<Double> GIANTS_RING_KNOCKBACK_RESISTANCE = BUILDER
            .comment("Bonus knockback resistance granted by Giant's Ring.")
            .defineInRange("giantsRingKnockbackResistance", 1, 0.0, 1.0);
    private static final ForgeConfigSpec.ConfigValue<Integer> DIVINE_SHIELD_COOLDOWN_TICKS = BUILDER
            .comment("Cooldown duration of the Divine Shield in ticks. (1 second = 20 ticks)")
            .defineInRange("divineShieldCooldownTicks", 400, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Integer> DIVINE_SHIELD_IMMUNITY_TICKS = BUILDER
            .comment("Duration of invulnerability after blocking damage with Divine Shield, in ticks. (1 second = 20 ticks)")
            .defineInRange("divineShieldImmunityTicks", 40, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<Double> RING_OF_AGILITY_CHANCE_SCALE = BUILDER
            .comment("Multiplier for movement speed in dodge chance formula.\n" +
                    "Formula: dodgeChance = 1.0 - pow(0.99, movementSpeed * 100 * scale)")
            .defineInRange("ringOfAgilityChanceScale", 1.0, 0.0, 100.0);
    private static final ForgeConfigSpec.ConfigValue<Double> RING_OF_AGILITY_MAX_DODGE_CHANCE = BUILDER
            .comment("Maximum dodge chance.\n" +
                    "Example: 0.9 = 90% maximum dodge chance.")
            .defineInRange("ringOfAgilityMaxDodgeChance", 0.9, 0.0, 1.0);
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double GravityCoreGravityMultiplier;
    public static double MoonGravityReduction;
    public static double GravityCoreImpactDamageCoefficient;
    public static double GravityCoreImpactRadiusCoefficient;
    public static int GravityCoreCooldown;
    public static double GravityCoreJumpStrength;
    public static double GiantsRingAttackDamage;
    public static double GiantsRingMaxHealth;
    public static double GiantsRingKnockbackResistance;
    public static int DivineShieldCooldownTicks;
    public static int DivineShieldImmunityTicks;
    public static double RingOfAgilityChanceScale;
    public static double RingOfAgilityMaxDodgeChance;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        MoonGravityReduction = MOON_GRAVITY_REDUCTION.get();
        GravityCoreGravityMultiplier = GRAVITY_CORE_GRAVITY_MULTIPLIER.get();
        GravityCoreImpactDamageCoefficient = GRAVITY_CORE_IMPACT_DAMAGE_COEFFICIENT.get();
        GravityCoreImpactRadiusCoefficient = GRAVITY_CORE_IMPACT_RADIUS_COEFFICIENT.get();
        GravityCoreCooldown = GRAVITY_CORE_COOLDOWN.get();
        GravityCoreJumpStrength = GRAVITY_CORE_JUMP_STRENGTH.get();
        GiantsRingAttackDamage = GIANTS_RING_ATTACK_DAMAGE.get();
        GiantsRingMaxHealth = GIANTS_RING_MAX_HEALTH.get();
        GiantsRingKnockbackResistance = GIANTS_RING_KNOCKBACK_RESISTANCE.get();
        DivineShieldCooldownTicks = DIVINE_SHIELD_COOLDOWN_TICKS.get();
        DivineShieldImmunityTicks = DIVINE_SHIELD_IMMUNITY_TICKS.get();
        RingOfAgilityChanceScale = RING_OF_AGILITY_CHANCE_SCALE.get();
        RingOfAgilityMaxDodgeChance = RING_OF_AGILITY_MAX_DODGE_CHANCE.get();
    }
}
