package dev.skyrotor.client;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.entity.HelicopterEntity;
import dev.skyrotor.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SkyRotor.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ClientInputEvents {
    private static int engineHoldTicks = 0;
    private static boolean engineRequestSent = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || !(player.getVehicle() instanceof HelicopterEntity)) {
            engineHoldTicks = 0;
            engineRequestSent = false;
            return;
        }

        if (minecraft.screen != null) {
            engineHoldTicks = 0;
            engineRequestSent = false;
            NetworkHandler.sendToServer(new NetworkHandler.FlightInputPacket(
                    0.0F, 0.0F, 0.0F, player.getYRot()));
            return;
        }

        // K: hold for 2 seconds (40 client ticks) to toggle the engine.
        if (ClientKeys.ENGINE.isDown()) {
            if (!engineRequestSent) {
                engineHoldTicks++;
                if (engineHoldTicks >= 40) {
                    NetworkHandler.sendToServer(new NetworkHandler.ToggleEnginePacket());
                    engineRequestSent = true;
                }
            }
        } else {
            engineHoldTicks = 0;
            engineRequestSent = false;
        }

        // V: drop a fireball bomb.
        while (ClientKeys.BOMB.consumeClick()) {
            NetworkHandler.sendToServer(new NetworkHandler.DropBombPacket());
        }

        float forward = (minecraft.options.keyUp.isDown() ? 1.0F : 0.0F)
                - (minecraft.options.keyDown.isDown() ? 1.0F : 0.0F);
        float strafe = (minecraft.options.keyRight.isDown() ? 1.0F : 0.0F)
                - (minecraft.options.keyLeft.isDown() ? 1.0F : 0.0F);

        // Read Space and Alt directly from GLFW. This avoids Minecraft key-mapping
        // conflicts (for example, Space being handled as the normal jump key).
        long window = minecraft.getWindow().getWindow();
        boolean ascend = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
        boolean descend = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;
        float lift = (ascend ? 1.0F : 0.0F) - (descend ? 1.0F : 0.0F);

        NetworkHandler.sendToServer(new NetworkHandler.FlightInputPacket(
                forward, strafe, lift, player.getYRot()));
    }

    private ClientInputEvents() {
    }
}
