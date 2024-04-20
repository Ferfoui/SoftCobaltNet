package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.socket.clientside.ClientSocketManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServerSocketManagerTest {

    public static final int PORT = 8080;

    public ClientSocketManager startAClient() {
        ClientSocketManager clientSocketManager = new ClientSocketManager(null);
        try {
            clientSocketManager.startConnection("localhost", PORT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start connection", e);
        }
        return clientSocketManager;
    }

    @Test
    public void testStartServer() throws IOException {

        Thread serverThread = new Thread(() -> {
            try {
                ServerSocketManager serverSocketManager = new ServerSocketManager("server-socket", SampleClientConnection::new);
                serverSocketManager.start(PORT);
                serverSocketManager.joinClientHandlers();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to start server", e);
            }
        });

        serverThread.start();

        ClientSocketManager client = startAClient();

        DataFormatter formatter = new DataFormatter();
        byte[] bytesToSend = formatter.createStringRequest("Hello, server!");
        client.sendBytes(bytesToSend);


    }

}
