package fr.ferfoui.softcobalt.client;

import fr.ferfoui.softcobalt.api.socket.clientside.ClientSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientMain {

    private static final String SERVER_IP = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);
    private static final ClientSocketManager clientSocketManager = new ClientSocketManager(logger);

    public static void main(String[] args) throws IOException {
        clientSocketManager.startConnection(SERVER_IP, Constants.SERVER_PORT);

        for (int i = 0; i < 10; i++) {
            String response = clientSocketManager.sendMessage("Hello from client " + i);
            logger.info("Received response: {}", response);
        }

        clientSocketManager.sendMessage("exit");
    }
}