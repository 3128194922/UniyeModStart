package com.Uniye.Uniyesmod.entity;

import com.Uniye.Uniyesmod.Item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class SeekingArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ARC_TOWARDS_ENTITY_ID =
            SynchedEntityData.defineId(SeekingArrowEntity.class, EntityDataSerializers.INT);
    private boolean stopSeeking;

    public SeekingArrowEntity(EntityType<? extends SeekingArrowEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
    }


    public SeekingArrowEntity(EntityType<? extends SeekingArrowEntity> type, Level level) {
        super(type, level);
    }

    public SeekingArrowEntity(Level level, LivingEntity shooter) {
        super(ModEntities.SEEKING_ARROW.get(), shooter, level);
    }

    public SeekingArrowEntity(Level level, double x, double y, double z) {
        super(ModEntities.SEEKING_ARROW.get(), x, y, z, level);
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

    @Override
    public void tick() {
        super.tick();
        if (!inGround && !stopSeeking) {
            int id = this.getArcTowardsID();

            if (id == -1 && !level().isClientSide) {
                Entity closest = null;
                Entity owner = this.getOwner();
                float radius = Math.min(10, 3 + this.tickCount / 4F);
                for (Entity entity : level().getEntities(this, this.getBoundingBox().inflate(radius), this::canHitEntity)) {
                    if ((closest == null || entity.distanceTo(this) < closest.distanceTo(this))
                            && !ownedBy(entity) && (owner == null || !entity.isAlliedTo(owner))) {
                        closest = entity;
                    }
                }
                if (closest != null) {
                    this.setArcTowardsID(closest.getId());
                }
            } else {
                Entity target = level().getEntity(id);
                if (target != null) {
                    Vec3 targetVec = target.position()
                            .add(0, 0.65F * target.getBbHeight(), 0)
                            .subtract(this.position());

                    if (targetVec.length() > target.getBbWidth()) {
                        Vec3 newMotion = this.getDeltaMovement()
                                .scale(0.3)
                                .add(targetVec.normalize().scale(0.7));
                        this.setDeltaMovement(newMotion);
                    }
                }
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        stopSeeking = true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.SEEKING_ARROW.get());
    }

    private int getArcTowardsID() {
        return this.entityData.get(ARC_TOWARDS_ENTITY_ID);
    }

    private void setArcTowardsID(int id) {
        this.entityData.set(ARC_TOWARDS_ENTITY_ID, id);
    }
}