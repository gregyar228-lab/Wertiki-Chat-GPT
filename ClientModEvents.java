package dev.skyrotor.client;

import dev.skyrotor.SkyRotor;
import dev.skyrotor.client.model.HelicopterModel;
import dev.skyrotor.client.render.HelicopterRenderer;
import dev.skyrotor.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyRotor.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HELICOPTER.get(), HelicopterRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HelicopterModel.LAYER_LOCATION, HelicopterModel::createBodyLayer);
    }

    private ClientModEvents() { }
}
