package dev.skyrotor.network;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.entity.HelicopterEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class NetworkHandler {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SkyRotor.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        int id = 0;
        CHANNEL.registerMessage(id++, ToggleEnginePacket.class,
                (message, buffer) -> { },
                buffer -> new ToggleEnginePacket(),
                ToggleEnginePacket::handle);
        CHANNEL.registerMessage(id++, DropBombPacket.class,
                (message, buffer) -> { },
                buffer -> new DropBombPacket(),
                DropBombPacket::handle);
        CHANNEL.registerMessage(id, FlightInputPacket.class,
                FlightInputPacket::encode,
                FlightInputPacket::decode,
                FlightInputPacket::handle);
        registered = true;
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    private static HelicopterEntity getPilotedHelicopter(ServerPlayer player) {
        if (player != null && player.getVehicle() instanceof HelicopterEntity helicopter
                && helicopter.isPilot(player)) {
            return helicopter;
        }
        return null;
    }

    public static class ToggleEnginePacket {
        public ToggleEnginePacket() { }

        private static void handle(ToggleEnginePacket message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                HelicopterEntity helicopter = getPilotedHelicopter(player);
                if (helicopter != null) helicopter.toggleEngine(player);
            });
            context.setPacketHandled(true);
        }
    }

    public static class DropBombPacket {
        public DropBombPacket() { }

        private static void handle(DropBombPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                HelicopterEntity helicopter = getPilotedHelicopter(player);
                if (helicopter != null) helicopter.dropBomb(player);
            });
            context.setPacketHandled(true);
        }
    }

    public static class FlightInputPacket {
        private final float forward;
        private final float strafe;
        private final float lift;
        private final float yaw;

        public FlightInputPacket(float forward, float strafe, float lift, float yaw) {
            this.forward = forward;
            this.strafe = strafe;
            this.lift = lift;
            this.yaw = yaw;
        }

        private static void encode(FlightInputPacket message, FriendlyByteBuf buffer) {
            buffer.writeFloat(message.forward);
            buffer.writeFloat(message.strafe);
            buffer.writeFloat(message.lift);
            buffer.writeFloat(message.yaw);
        }

        private static FlightInputPacket decode(FriendlyByteBuf buffer) {
            return new FlightInputPacket(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }

        private static void handle(FlightInputPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                HelicopterEntity helicopter = getPilotedHelicopter(player);
                if (helicopter != null
                        && Float.isFinite(message.forward)
                        && Float.isFinite(message.strafe)
                        && Float.isFinite(message.lift)
                        && Float.isFinite(message.yaw)) {
                    helicopter.acceptPilotInput(player, message.forward, message.strafe, message.lift, message.yaw);
                }
            });
            context.setPacketHandled(true);
        }
    }

    private NetworkHandler() { }
}
