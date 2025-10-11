package com.Uniye.Uniyesmod.entity;

import com.Uniye.Uniyesmod.Item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class NetherOfVoiceEntity extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<Integer> ARC_TOWARDS_ENTITY_ID =
            SynchedEntityData.defineId(NetherOfVoiceEntity.class, EntityDataSerializers.INT);
    private boolean stopSeeking;

    public NetherOfVoiceEntity(EntityType<? extends NetherOfVoiceEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
    }

    public NetherOfVoiceEntity(EntityType<? extends NetherOfVoiceEntity> type, Level level) {
        super(type, level);
    }

    public NetherOfVoiceEntity(Level level, LivingEntity shooter) {
        super(ModEntities.NETHEROFVOICE.get(), shooter, level);
    }

    public NetherOfVoiceEntity(Level level, double x, double y, double z) {
        super(ModEntities.NETHEROFVOICE.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARC_TOWARDS_ENTITY_ID, -1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // 禁用原版箭矢的暴击粒子和拖尾
    @Override
    public boolean isCritArrow() {
        return false;
    }

    @Override
    public void setCritArrow(boolean critArrow) {
        // 不允许被设置为 crit
    }


    @Override
    public void tick() {
        super.tick();

        // 客户端：音符粒子（每 3 tick 一次；落地或 stopSeeking 后不再产生）
        if (this.level().isClientSide && !this.inGround && !this.stopSeeking && this.tickCount % 3 == 0) {
            double x = this.getX();
            double y = this.getY() + 0.1D;
            double z = this.getZ();
            float pitch = this.random.nextFloat(); // 0.0 ~ 1.0

            this.level().addParticle(
                    ParticleTypes.NOTE,
                    x, y, z,
                    pitch, 0.0D, 0.0D
            );
        }

        // 简化版追踪逻辑
        if (!this.inGround && !this.stopSeeking) {
            int id = this.getArcTowardsID();

            if (id == -1 && !this.level().isClientSide) {
                Entity closest = null;
                Entity owner = this.getOwner();
                float radius = Math.min(10, 3 + this.tickCount / 4F);

                for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(radius), e -> isEnemy(owner, e))) {
                    if (closest == null || entity.distanceTo(this) < closest.distanceTo(this)) {
                        closest = entity;
                    }
                }

                if (closest != null) {
                    this.setArcTowardsID(closest.getId());
                }
            } else {
                Entity target = this.level().getEntity(id);
                if (target != null) {
                    Vec3 targetVec = target.position()
                            .add(0, 0.65F * target.getBbHeight(), 0)
                            .subtract(this.position());

                    if (targetVec.length() > target.getBbWidth()) {
                        Vec3 newMotion = this.getDeltaMovement()
                                .scale(0.15)
                                .add(targetVec.normalize().scale(0.35));
                        this.setDeltaMovement(newMotion);
                    }
                }
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        // 命中实体后：停止寻的，不播放音效
        this.stopSeeking = true;
        // 不调用 super.doPostHurtEffects(entity)，避免默认行为
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // 碰到方块后直接消失，不播放音效
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // 保持原本的伤害逻辑
        Entity target = result.getEntity();
        if (target instanceof LivingEntity living) {
            // 调用 AbstractArrow 的 doPostHurtEffects，但不要触发声音
            this.doPostHurtEffects(living);
        }

        // 执行伤害
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity livingOwner) {
            target.hurt(this.damageSources().arrow(this, livingOwner), (float)this.getBaseDamage());
        } else {
            target.hurt(this.damageSources().arrow(this, this), (float)this.getBaseDamage());
        }

        // 命中后让箭失效（保持你之前的 stopSeeking 行为）
        this.stopSeeking = true;
    }


    // 屏蔽命中方块音效
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return null;
    }


    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.NETHEROFVOICE.get());
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.NETHEROFVOICE.get());
    }

    private int getArcTowardsID() {
        return this.entityData.get(ARC_TOWARDS_ENTITY_ID);
    }

    private void setArcTowardsID(int id) {
        this.entityData.set(ARC_TOWARDS_ENTITY_ID, id);
    }

    private boolean isEnemy(Entity owner, Entity entity) {
        if (!(entity instanceof LivingEntity target)) return false;
        if (!target.isAlive()) return false;

        if (entity == owner) return false;

        if (owner instanceof LivingEntity livingOwner) {
            if (livingOwner.getTeam() != null && target.getTeam() != null &&
                    livingOwner.getTeam().equals(target.getTeam())) {
                return false;
            }
        }

        if (target instanceof TamableAnimal tamable) {
            if (owner instanceof LivingEntity livingOwner && tamable.isOwnedBy(livingOwner)) {
                return false;
            }
        }

        return true;
    }
}
