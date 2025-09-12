package com.Uniye.Uniyesmod.entity;

import com.Uniye.Uniyesmod.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AirburstArrowEntity extends AbstractArrow {

    private int tickCount = 0;
    private Vec3 shooterPos = null;

    public AirburstArrowEntity(EntityType<? extends AirburstArrowEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED; // 不可拾取
    }

    public AirburstArrowEntity(EntityType<? extends AirburstArrowEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
        this.pickup = Pickup.DISALLOWED;
        this.shooterPos = shooter.position();
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
        super.onHitEntity(result);
        explode();
    }

    @Override
    protected void onHitBlock(net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        explode();
    }

    @Override
    public void tick() {
        super.tick();

        tickCount++;

        // 如果发射者坐标还没记录（例如从 NBT 恢复），记录一次
        if (shooterPos == null && this.getOwner() != null) {
            shooterPos = this.getOwner().position();
        }

        // 检查是否满足空爆条件
        if (tickCount >= 60 || (shooterPos != null && this.position().distanceToSqr(shooterPos) > 400)) {
            explode();
        }
    }
/*
    private void explode() {
        if (level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        int count = 12 + this.random.nextInt(8); // 8~12 个子箭

        Entity ownerEntity = this.getOwner();
        LivingEntity owner = null;
        if (ownerEntity instanceof LivingEntity living) {
            owner = living;
        }

        for (int i = 0; i < count; i++) {
            // 基础向量向下
            //Vec3 base = new Vec3(0, -1, 0);
            Vec3 base = new Vec3(0, -0.8, 0); // 稍微抬头

            // 稍微扰动的随机向量（增强自然感）
            Vec3 randomVec = new Vec3(
                    (random.nextDouble() - 0.5),
                    -(random.nextDouble() * 0.5 + 0.5),
                    (random.nextDouble() - 0.5)
            ).normalize();

            Vec3 finalVec = base.add(randomVec).normalize().scale(1.5); // 最终速度向量

            ExplodingArrowEntity childArrow = new ExplodingArrowEntity(ModEntities.EXPLODING_ARROW.get(), serverLevel, owner);
            childArrow.setPos(this.getX(), this.getY(), this.getZ());
            childArrow.setDeltaMovement(finalVec);
            serverLevel.addFreshEntity(childArrow);
        }


        this.discard();
    }
*/
private void explode() {
    if (level().isClientSide()) return;

    ServerLevel serverLevel = (ServerLevel) this.level();

    int count = Config.AirBurstNumber + this.random.nextInt(Config.AirBurstNumberRandom); // 8~16 个子箭

    Entity ownerEntity = this.getOwner();
    LivingEntity owner = null;
    if (ownerEntity instanceof LivingEntity living) {
        owner = living;
    }

    for (int i = 0; i < count; i++) {
        // 生成单位球面上的随机方向向量
        double theta = random.nextDouble() * 2 * Math.PI; // 水平角 0~2π
        double phi = Math.acos(2 * random.nextDouble() - 1); // 垂直角 0~π

        double dx = Math.sin(phi) * Math.cos(theta)* 0.3;
        double dy = Math.cos(phi) * 0.3; // dy向上分量缩小为0.3倍
        double dz = Math.sin(phi) * Math.sin(theta)* 0.3;

        Vec3 finalVec = new Vec3(dx, dy, dz).scale(1.5); // 速度向量，长度1.5

        ExplodingArrowEntity childArrow = new ExplodingArrowEntity(ModEntities.EXPLODING_ARROW.get(), serverLevel, owner);
        childArrow.setPos(this.getX(), this.getY(), this.getZ());
        childArrow.setDeltaMovement(finalVec);
        serverLevel.addFreshEntity(childArrow);
    }

    this.discard();
}


    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("tickCount", tickCount);
        if (shooterPos != null) {
            tag.putDouble("shooterX", shooterPos.x);
            tag.putDouble("shooterY", shooterPos.y);
            tag.putDouble("shooterZ", shooterPos.z);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tickCount = tag.getInt("tickCount");
        if (tag.contains("shooterX")) {
            shooterPos = new Vec3(tag.getDouble("shooterX"), tag.getDouble("shooterY"), tag.getDouble("shooterZ"));
        }
    }
}
