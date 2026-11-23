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

@Mod.EventBusSubscriber(modid = SkyRotor.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ClientInputEvents {
    private static int engineHoldTicks = 0;
    private static boolean engineRequestSent = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

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
            NetworkHandler.sendToServer(new NetworkHandler.FlightInputPacket(0.0F, 0.0F, 0.0F, player.getYRot()));
            return;
        }

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

        while (ClientKeys.BOMB.consumeClick()) {
            NetworkHandler.sendToServer(new NetworkHandler.DropBombPacket());
        }

        float forward = (minecraft.options.keyUp.isDown() ? 1.0F : 0.0F)
                - (minecraft.options.keyDown.isDown() ? 1.0F : 0.0F);
        float strafe = (minecraft.options.keyRight.isDown() ? 1.0F : 0.0F)
                - (minecraft.options.keyLeft.isDown() ? 1.0F : 0.0F);
        float lift = (minecraft.options.keyJump.isDown() ? 1.0F : 0.0F)
                - (ClientKeys.DESCEND.isDown() ? 1.0F : 0.0F);

        NetworkHandler.sendToServer(new NetworkHandler.FlightInputPacket(forward, strafe, lift, player.getYRot()));
    }

    private ClientInputEvents() { }
}
