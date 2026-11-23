package dev.skyrotor;

import dev.skyrotor.registry.ModEntities;
import dev.skyrotor.registry.ModItems;
import dev.skyrotor.registry.ModTabs;
import dev.skyrotor.network.NetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SkyRotor.MOD_ID)
public class SkyRotor {
    public static final String MOD_ID = "skyrotor";

    public SkyRotor() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTabs.TABS.register(modBus);
        NetworkHandler.register();
    }
}
