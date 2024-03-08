package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.socket.serverside.ClientSocketHandler;

public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        SocketServerThread serverThread = new SocketServerThread();
        serverThread.start();

        boolean clientClosed = false;

        while (!clientClosed) {
            Thread.sleep(1000);
            for (ClientSocketHandler clientHandler : serverThread.getClientHandlers()) {
                clientClosed = clientHandler.isClosed();
            }
        }

        serverThread.stopServer();
    }


}