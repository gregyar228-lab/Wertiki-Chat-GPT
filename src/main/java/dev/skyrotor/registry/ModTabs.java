package dev.skyrotor.registry;

import dev.skyrotor.SkyRotor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkyRotor.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SKYROTOR_TAB = TABS.register(
            "skyrotor",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.skyrotor"))
                    .icon(() -> new ItemStack(ModItems.HELICOPTER_SPAWNER.get()))
                    .displayItems((parameters, output) -> output.accept(ModItems.HELICOPTER_SPAWNER.get()))
                    .build()
    );

    private ModTabs() { }
}
