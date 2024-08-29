package eu.michalkopecky.tiktok_live_game_connector;
import java.io.IOException;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/command")
public class CommandEndpoint {
    private static Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        CommandEndpoint.session = session;
        session.getBasicRemote().sendText("onOpen");
        System.out.println("onOpen");
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    public static void sendCommand(String command) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(command);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No open session to send a message.");
        }
    }
}
