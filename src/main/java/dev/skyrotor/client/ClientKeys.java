package dev.skyrotor.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.skyrotor.SkyRotor;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SkyRotor.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientKeys {
    static final KeyMapping ENGINE = new KeyMapping(
            "key.skyrotor.engine", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.categories.skyrotor");
    static final KeyMapping BOMB = new KeyMapping(
            "key.skyrotor.bomb", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.skyrotor");
    static final KeyMapping DESCEND = new KeyMapping(
            "key.skyrotor.descend", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.categories.skyrotor");


    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(ENGINE);
        event.register(BOMB);
        event.register(DESCEND);
    }

    private ClientKeys() { }
}
