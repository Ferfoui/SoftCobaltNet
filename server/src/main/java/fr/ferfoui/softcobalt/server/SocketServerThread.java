package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.socket.serverside.ClientSocketHandler;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;

import java.io.IOException;
import java.util.ArrayList;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager = new ServerSocketManager();

    SocketServerThread() {
        super("SocketServerThread");
    }

    @Override
    public void run() {
        try {
            serverSocketManager.start(Constants.SERVER_PORT, (in, out) -> {
                String request = null;
                try {
                    request = in.readLine();
                    System.out.println("Received request: " + request);
                    out.println("Server response for: '" + request + "'");
                } catch (IOException e) {
                    System.out.println("Error reading request: " + e.getMessage());
                }
                return request != null && !request.equals("exit");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            serverSocketManager.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClientSocketHandler> getClientHandlers() {
        return serverSocketManager.getClientHandlers();
    }
}
