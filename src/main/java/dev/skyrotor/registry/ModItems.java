package dev.skyrotor.registry;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.item.HelicopterSpawnerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SkyRotor.MOD_ID);

    public static final RegistryObject<Item> HELICOPTER_SPAWNER = ITEMS.register(
            "helicopter_spawner",
            () -> new HelicopterSpawnerItem(new Item.Properties().stacksTo(1))
    );

    private ModItems() { }
}
