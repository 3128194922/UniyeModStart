package com.Uniye.Uniyesmod.entity;

import com.Uniye.Uniyesmod.Config;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class SlimeArrow extends AbstractArrow {

    public static float MAX_VELOCITY = 3.0F;
    public static double MIN_VELOCITY = Config.TNTArrowMinVelocity;
    public static double DAMAGE = Config.TNTArrowDamage;
    public static int BOUNCES = Config.TNTArrowBounces;

    private int curBounces = 0;

    public SlimeArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(DAMAGE);
    }

    public SlimeArrow(EntityType<? extends AbstractArrow> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
        this.setBaseDamage(DAMAGE);
    }

    @Override
    protected ItemStack getPickupItem() {
        // 返回箭头物品
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            if (curBounces >= BOUNCES || this.isInWater() || this.getDeltaMovement().lengthSqr() < MIN_VELOCITY * MIN_VELOCITY) {
                super.onHit(result);
                return;
            }

            BlockHitResult blockHit = (BlockHitResult) result;
            Vec3 motion = this.getDeltaMovement();

            // 根据命中面方向反弹
            switch (blockHit.getDirection()) {
                case UP, DOWN -> this.setDeltaMovement(motion.x, -motion.y, motion.z);
                case NORTH, SOUTH -> this.setDeltaMovement(motion.x, motion.y, -motion.z);
                case EAST, WEST -> this.setDeltaMovement(-motion.x, motion.y, motion.z);
            }

            // 爆炸效果：每次反弹都爆炸，但不破坏方块
            level().explode(
                    this,                      // 爆炸源
                    this.getX(), this.getY(), this.getZ(),
                    1.5f,                      // 爆炸强度（TNT 默认是 4.0）
                    Level.ExplosionInteraction.NONE // 不破坏地形
            );

            // 方向旋转同步
            double horiz = motion.horizontalDistance();
            this.setXRot((float) (Mth.atan2(motion.y, horiz) * (180F / Math.PI)));
            this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180F / Math.PI)));

            // 音效
            this.playSound(SoundEvents.SLIME_BLOCK_HIT, 1.0F, 1.0F);

            // 反弹次数++
            curBounces++;

        } else {
            super.onHit(result);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.inGround) {
            Vec3 motion = this.getDeltaMovement();
            this.level().addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), -motion.x, -motion.y + 0.2D, -motion.z);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Bounces", curBounces);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.curBounces = tag.getInt("Bounces");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
