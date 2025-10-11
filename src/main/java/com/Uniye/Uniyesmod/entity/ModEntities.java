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
    public static final RegistryObject<EntityType<NetherOfVoiceEntity>> NETHEROFVOICE = ENTITIES.register("nether_of_voice",
            () -> EntityType.Builder.<NetherOfVoiceEntity>of(NetherOfVoiceEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("nether_of_voice")
    );
    public static final RegistryObject<EntityType<AirburstArrowEntity>> AIRBURST_ARROW = ENTITIES.register("airburst_arrow",
            () -> EntityType.Builder.<AirburstArrowEntity>of(AirburstArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("airburst_arrow")
    );
    public static final RegistryObject<EntityType<ExplodingArrowEntity>> EXPLODING_ARROW = ENTITIES.register("exploding_arrow",
            () -> EntityType.Builder.<ExplodingArrowEntity>of(ExplodingArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("exploding_arrow")
    );
    public static final RegistryObject<EntityType<FinalExplodingArrowEntity>> FINAL_EXPLODING_ARROW = ENTITIES.register("final_exploding_arrow",
            () -> EntityType.Builder.<FinalExplodingArrowEntity>of(FinalExplodingArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("final_exploding_arrow")
    );

}
