package fr.ferfoui.softcobalt.client;

import fr.ferfoui.softcobalt.api.socket.clientside.ClientSocketManager;
import fr.ferfoui.softcobalt.common.Constants;

import java.io.IOException;

public class ClientMain {

    static final String SERVER_IP = "127.0.0.1";

    static final ClientSocketManager clientSocketManager = new ClientSocketManager();

    public static void main(String[] args) throws IOException {
        clientSocketManager.startConnection(SERVER_IP, Constants.SERVER_PORT);

        for (int i = 0; i < 10; i++) {
            String response = clientSocketManager.sendMessage("Hello from client " + i);
            System.out.println("Received response: " + response);
        }

        clientSocketManager.sendMessage("exit");
    }
}