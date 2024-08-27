package eu.michalkopecky.tiktoklivemod;

import com.mojang.logging.LogUtils;
import eu.michalkopecky.tiktoklivemod.events.EventHandler;
import io.github.jwdeveloper.tiktok.TikTokLive;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TikTokLiveMod.MOD_ID)
public class TikTokLiveMod {
    public static final String MOD_ID = "tiktoklivemod";
    public static final String TIKTOK_HOSTNAME = "carlo_q";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TikTokLiveMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(EventHandler.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        sendGlobalMessage(Component.literal("Ahoj, světe."));
    }

    private static void sendGlobalMessage(Component message) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.sendSystemMessage(message);
            }
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        var giftsManager = TikTokLive.gifts();

       /*TikTokLive.newClient("carlo_q")
               .onJoin(((liveClient, tikTokEvent) -> {
                   sendGlobalMessage(Component.literal(tikTokEvent.getUser().getName() + "has joined."));
               }))
                .buildAndConnect();*/
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
