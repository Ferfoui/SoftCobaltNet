package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.socket.clientside.ClientSocketManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Thread serverThread = getServerThread();
        serverThread.start();

        ClientSocketManager client = startAClient();

        DataFormatter formatter = new DataFormatter();
        byte[] bytesToSend = formatter.createStringRequest("Hello, server!");
        client.sendBytes(bytesToSend);

        String response1 = new String(new DataReader(client.waitUntilDataAvailable()).readBody());

        bytesToSend = formatter.createStringRequest(SampleStringClientConnection.EXIT_COMMAND);
        client.sendBytes(bytesToSend);

        String response2 = new String(new DataReader(client.waitUntilDataAvailable()).readBody());

        serverThread.interrupt();

        assertEquals("Server response for: 'Hello, server!'", response1);
        assertEquals("Server response for: 'exit'", response2);
    }

    private static Thread getServerThread() {
        return new Thread( () -> {
            try {
                ServerSocketManager serverSocketManager = new ServerSocketManager("server-socket", SampleStringClientConnection::new);
                serverSocketManager.start(PORT);
                serverSocketManager.stopClientHandlers();
                serverSocketManager.stop();
            } catch (IOException e) {
                throw new RuntimeException("Failed to start server", e);
            }
        }, "server_thread");
    }

}
