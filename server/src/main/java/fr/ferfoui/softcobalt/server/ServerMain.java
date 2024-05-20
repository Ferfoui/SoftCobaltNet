package fr.ferfoui.softcobalt.server;

public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        SocketServerThread serverThread = new SocketServerThread();
        serverThread.start();

        boolean clientClosed = false;

        while (!clientClosed) {
            Thread.sleep(1000);
            clientClosed = serverThread.isClientHandlersTerminated();
        }

        serverThread.stopServer();
        serverThread.interrupt();
    }


}