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

public class ExplodingArrowEntity extends AbstractArrow {

    public ExplodingArrowEntity(EntityType<? extends ExplodingArrowEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public ExplodingArrowEntity(EntityType<? extends ExplodingArrowEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
        this.pickup = Pickup.DISALLOWED;
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

    private void explode() {
        if (level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        // 原版爆炸（不破坏方块、不点燃）
        serverLevel.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, Level.ExplosionInteraction.NONE);

        int count = Config.AirBurstNumber2 + this.random.nextInt(Config.AirBurstNumber2Random); // 6~10 个小箭
        Entity ownerEntity = this.getOwner();
        LivingEntity owner = null;
        if (ownerEntity instanceof LivingEntity living) owner = living;

        for (int i = 0; i < count; i++) {
            // 原始分量
            double dx = (random.nextDouble() - 0.5) * 0.3; // 控制横向扩散范围
            double dy = (random.nextDouble() * 0.2 + 0.05); // 控制向上分量，0.05 ~ 0.25
            double dz = (random.nextDouble() - 0.5) * 0.3;

            Vec3 randomDir = new Vec3(dx, dy, dz);

            FinalExplodingArrowEntity smallArrow = new FinalExplodingArrowEntity(ModEntities.FINAL_EXPLODING_ARROW.get(), serverLevel, owner);
            smallArrow.setPos(this.getX(), this.getY(), this.getZ());
            smallArrow.setDeltaMovement(randomDir);

            serverLevel.addFreshEntity(smallArrow);
        }

        this.discard();
    }





    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    // 可根据需要添加NBT存取方法
}
