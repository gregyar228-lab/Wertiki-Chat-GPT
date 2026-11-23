package dev.skyrotor.registry;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.entity.HelicopterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkyRotor.MOD_ID);

    public static final RegistryObject<EntityType<HelicopterEntity>> HELICOPTER = ENTITY_TYPES.register(
            "helicopter",
            () -> EntityType.Builder.<HelicopterEntity>of(HelicopterEntity::new, MobCategory.MISC)
                    .sized(3.0F, 2.0F)
                    .clientTrackingRange(12)
                    .updateInterval(1)
                    .build("helicopter")
    );

    private ModEntities() { }
}
