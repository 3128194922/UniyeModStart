package com.Uniye.Uniyesmod.Item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.SporeCloud;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GrieferSpearItem extends SpearItem{
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;


    // 新增冲刺相关字段
    private static final int DASH_COOLDOWN = 120;
    private static final float DASH_SPEED = 2.0F;
    private static final float EXPLOSION_RADIUS = 2.0F;
    private int dashTicks = 0;
    private boolean isDashing = false;



    public GrieferSpearItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);

        this.attackDamage = attackDamage;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier("Weapon modifier", 2.0D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return !repair.isEmpty() && repair.getItem() == SRItems.BLAST_PROOF_PLATING.get();
    }


    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return !player.isCreative();
    }

//因为是钝器，所以不能破坏蜘蛛网

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity user) {
        stack.hurtAndBreak(1, user, (player) -> {
            player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        // 检查攻击冷却时间
        if (user instanceof Player player && player.getAttackStrengthScale(0.5F) < 1.0F) {
            return true;
        }

        // 检查玩家是否有苦力怕孢子
        if (user instanceof Player player) {
            boolean hasSPORES = false;
            for (ItemStack item : player.getInventory().items) {
                if (item.getItem() == SRItems.CREEPER_SPORES.get()) {
                    item.shrink(1);
                    hasSPORES = true;
                    break;
                }
            }
            if (!hasSPORES) {
                return true;
            }
        }

        // 生成苦力怕孢子
        if (!enemy.level().isClientSide) {
            SporeCloud sporeCloud = new SporeCloud(
                    SREntityTypes.SPORE_CLOUD.get(),
                    enemy.level()
            );
            sporeCloud.setOwner(user);
            sporeCloud.setPos(enemy.getX(), enemy.getY(), enemy.getZ());
            sporeCloud.setDeltaMovement(
                    (enemy.getRandom().nextDouble() - 0.5) * 0.1,
                    enemy.getRandom().nextDouble() * 0.1,
                    (enemy.getRandom().nextDouble() - 0.5) * 0.1
            );
            enemy.level().addFreshEntity(sporeCloud);
        }

        return true;
    }



    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (isDashing && entity instanceof Player player) {
            // 检测到Y轴移动时立即终止冲刺
            if (Math.abs(player.getDeltaMovement().y) > 0.1) {
                isDashing = false;
                return;
            }
            handleDashing(player, world);
        }
    }



    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return isFirstPersonView() ? UseAnim.SPEAR : UseAnim.BRUSH;
    }
    private boolean isFirstPersonView() {
        // 实现第一人称判断逻辑
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }




    @Override
    public int getUseDuration(ItemStack stack) {
        return 600; // 长按最大持续时间
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // 仅允许主手触发
        if(hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int timeLeft) {
        if (user instanceof Player player && !world.isClientSide) {
            int useDuration = this.getUseDuration(stack) - timeLeft;
            if (!ItemStack.matches(stack, player.getMainHandItem())) return;
            if (useDuration >= 10) { // 长按超过0.5秒触发冲刺
                startDashing(player);
                player.getCooldowns().addCooldown(this, DASH_COOLDOWN);
            }
        }
    }

    private void startDashing(Player player) {
        this.isDashing = true;
        this.dashTicks = 0;
        Vec3 look = player.getLookAngle();
        // 移除Y轴分量并标准化
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize().scale(DASH_SPEED);
        player.setDeltaMovement(horizontalLook);
        player.hurtMarked = true;
    }


    private void handleDashing(Player player, Level world) {
        dashTicks++;
        Vec3 currentMotion = player.getDeltaMovement();
        player.setDeltaMovement(new Vec3(
                currentMotion.x* 1.2,
                currentMotion.y,
                currentMotion.z* 1.2
        ));
        if (dashTicks > 30) {
            isDashing = false;
            return;
        }

        // 检测碰撞实体
        AABB aabb = player.getBoundingBox().inflate(1.0);
        List<LivingEntity> entities = world.getEntitiesOfClass(
                LivingEntity.class,
                aabb,
                e -> e.isAlive() && !e.equals(player) // 过滤玩家自身
        );

        // 触发爆炸效果
        if (!entities.isEmpty() && !world.isClientSide) {
            for (LivingEntity entity : entities) {
                entity.hurt(player.damageSources().playerAttack(player), 5.0F);
                world.explode(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        EXPLOSION_RADIUS,
                        Level.ExplosionInteraction.NONE

                );
            }
            isDashing = false;
        }
    }


    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(world, pos) != 0.0F) {
            stack.hurtAndBreak(2, entity, (player) -> {
                player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || (enchantment.category == EnchantmentCategory.WEAPON && enchantment != Enchantments.SWEEPING_EDGE);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book) && !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.SWEEPING_EDGE);
    }
}
