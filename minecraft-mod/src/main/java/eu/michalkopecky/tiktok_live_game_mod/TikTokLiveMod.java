package eu.michalkopecky.tiktok_live_game_mod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
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
    public static final Logger LOGGER = LogUtils.getLogger();
    private static ServerStartingEvent event;

    public TikTokLiveMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void sendGlobalMessage(String message) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendSystemMessage(Component.literal(message));
        }
    }

    private static void setUpWebSocketClient() {
        var webSocketClient = new WebSocketClient("ws://localhost:8080/ws/command");
    }

    public static void processCommand(String command) {
        LOGGER.info("Received command: " + command);
        sendGlobalMessage("Received command: " + command);

        if (event == null) {
            LOGGER.warn("Could not process command " + command + ", event instance is null.");
            return;
        }

        var overworld = event.getServer().getLevel(Level.OVERWORLD);
        if (overworld == null) {
            LOGGER.warn("Could not process command " + command + ", level instance is null.");
            return;
        }

        var player = overworld.getRandomPlayer();
        if (player == null) {
            LOGGER.warn("Could not process command " + command + ", player instance is null.");
            return;
        }

        var commandSplit = command.split(":");
        if (commandSplit.length != 2) {
            LOGGER.warn("Could not process command " + command + ", it has wrong syntax.");
            return;
        }

        switch (commandSplit[0]) {
            case "spawn_zombie":
                spawnEntity(overworld, player, commandSplit[1], EntityType.ZOMBIE);
                break;
            case "spawn_creeper":
                spawnEntity(overworld, player, commandSplit[1], EntityType.CREEPER);
                break;
            case "spawn_skeleton":
                spawnEntity(overworld, player, commandSplit[1], EntityType.SKELETON);
                break;
            default:
                LOGGER.warn("Unknown command: " + commandSplit[0]);
                break;
        }
    }

    private static void spawnEntity(ServerLevel level, ServerPlayer player, String playerName, EntityType<? extends LivingEntity> entityType) {
        LivingEntity entity = entityType.create(level);
        if (entity != null) {
            entity.setCustomName(Component.literal(playerName));
            entity.setPos(player.getX(), player.getY(), player.getZ());
            level.addFreshEntity(entity);

            LOGGER.info("Spawned " + entityType.getDescriptionId().split("\\.")[2] + ".");
        } else {
            LOGGER.warn("Failed to spawn entity of type: " + entityType.getDescriptionId().split("\\.")[2]);
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
        TikTokLiveMod.event = event;
        setUpWebSocketClient();
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
