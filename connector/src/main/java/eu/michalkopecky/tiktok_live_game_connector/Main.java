package eu.michalkopecky.tiktok_live_game_connector;

import javax.websocket.Session;
import io.github.jwdeveloper.tiktok.TikTokLive;
import org.glassfish.tyrus.server.Server;

import java.io.IOException;

public class Main {
    public static final String TIKTOK_HOSTNAME = "chuongdeoo";
    public static final String TIKTOK_SESSION = "acd0986fc04b6f71d92242e412c4c574";

    public static void main(String[] args) throws IOException {
        Server server = new Server("localhost", 8080, "/ws", null, CommandEndpoint.class);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:8080/ws");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TikTokLive.newClient(TIKTOK_HOSTNAME)
                .configure((settings) -> {
                    settings.setSessionId(TIKTOK_SESSION);
                })
                .onLike((liveClient, event) -> {
                    System.out.println("[LIKE] " + event.getUser().getName());
                    CommandEndpoint.sendCommand("spawn_zombie:" + event.getUser().getName());
                })
                .onGift((liveClient, event) ->
                {
                    System.out.println("[GIFT] " + event.getUser().getName() + ":" + event.getGift().getName());
                    CommandEndpoint.sendCommand("spawn_creeper:" + event.getUser().getName());
                })
                .onComment((liveClient, event) ->
                {
                    System.out.println("[COMMENT] " + event.getUser().getName() + ":" + event.getText());
                    CommandEndpoint.sendCommand("spawn_skeleton:" + event.getUser().getName());
                })
                .buildAndConnectAsync();

        System.in.read();
    }
}
