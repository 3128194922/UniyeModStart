package com.Uniye.Uniyesmod.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "uniyesmod");

    public static final RegistryObject<EntityType> SLIME_ARROW = ENTITIES.register("slime_arrow",
            () -> EntityType.Builder.<SlimeArrow>of(SlimeArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("slime_arrow")
    );
    public static final RegistryObject<EntityType<SeekingArrowEntity>> SEEKING_ARROW = ENTITIES.register("seeking_arrow",
            () -> EntityType.Builder.<SeekingArrowEntity>of(SeekingArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("seeking_arrow")
    );
}
