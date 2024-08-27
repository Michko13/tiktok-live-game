package eu.michalkopecky.tiktoklivemod.events;

import io.github.jwdeveloper.tiktok.TikTokLive;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class EventHandler {
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        /*LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide()) {
            entity.setHealth(0.0F); // Set health to 0, effectively killing the entity
        }*/
    }
}