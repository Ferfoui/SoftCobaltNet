package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerSocketManager {

    private final Logger logger;

    private ServerSocket serverSocket;
    private boolean running;
    private final ArrayList<ClientSocketHandler> clientHandlers = new ArrayList<>();

    /**
     * Constructor for the ServerSocketManager
     * @param name The name of the logger to use
     */
    public ServerSocketManager(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    /**
     * Start the server on the given port
     * @param port The port to start the server on
     * @param serverLogic The logic to process the requests
     * @throws IOException If the server socket cannot be created
     */
    public void start(int port, RequestProcessor serverLogic) throws IOException {
        logger.info("Starting server on port: {}", port);
        serverSocket = new ServerSocket(port);
        running = true;
        while (running) {
            try {
                ClientSocketHandler clientHandler = new ClientSocketHandler(serverSocket.accept(), port, serverLogic, null);
                clientHandler.start();
                clientHandlers.add(clientHandler);
            } catch (SocketException e) {
                // The Socket was closed while accept() was waiting, break the loop
                if (!running) {
                    break;
                }
            }
        }
    }

    /**
     * Get the list of client handlers
     * @return The list of client handlers
     */
    public ArrayList<ClientSocketHandler> getClientHandlers() {
        return clientHandlers;
    }

    /**
     * Stop the server
     * @throws IOException If the server socket cannot be closed
     */
    public void stop() throws IOException {
        logger.info("Shutting down Socket server");
        running = false;
        stopClientHandlers();
        serverSocket.close();
    }

    /**
     * Stop all client handlers
     */
    public void stopClientHandlers() {
        logger.info("Shutting down client handlers");
        // Use an iterator to avoid ConcurrentModificationException
        Iterator<ClientSocketHandler> iterator = clientHandlers.iterator();
        while (iterator.hasNext()) {
            ClientSocketHandler clientHandler = iterator.next();
            clientHandler.interrupt();
            iterator.remove();
        }
    }
}
