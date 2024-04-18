package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class used to manage the server socket connection.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ServerSocketManager {

    private final Logger logger;

    private ServerSocket serverSocket;
    private boolean doContinueAccept;
    private final ArrayList<ClientSocketHandler> clientHandlers = new ArrayList<>();

    ClientConnectionProvider clientConnectionProvider;

    /**
     * Constructor for the ServerSocketManager
     *
     * @param name The name of the logger to use
     */
    public ServerSocketManager(String name, ClientConnectionProvider clientConnectionProvider) {
        logger = LoggerFactory.getLogger(name);
        this.clientConnectionProvider = clientConnectionProvider;
    }

    /**
     * Start the server on the given port
     *
     * @param port The port to start the server on
     * @throws IOException If the server socket cannot be created
     */
    public void start(int port) throws IOException {
        logger.info("Starting server on port: {}", port);
        serverSocket = new ServerSocket(port);
        acceptConnections();
    }

    private void acceptConnections() throws IOException {
        doContinueAccept = true;

        int clientId = 0;
        while (doContinueAccept) {
            try {
                Socket connectionSocket = serverSocket.accept();
                logger.info("Accepted connection from: {}", connectionSocket.getInetAddress());

                ClientSocketHandler clientHandler = new ClientSocketHandler(
                        connectionSocket,
                        getNewClientConnection(connectionSocket, clientId),
                        clientId, null);

                clientHandler.start();
                clientHandlers.add(clientHandler);
                clientId++;
            } catch (SocketException e) {
                // The Socket was closed while accept() was waiting, break the loop
                if (!doContinueAccept) {
                    break;
                } else {
                    logger.error("An error occurred while accepting a connection", e);
                }
            }
        }
    }

    /**
     * Get the list of client handlers
     *
     * @return The list of client handlers
     */
    public ArrayList<ClientSocketHandler> getClientHandlers() {
        return clientHandlers;
    }

    /**
     * Stop the server
     *
     * @throws IOException If the server socket cannot be closed
     */
    public void stop() throws IOException {
        logger.info("Shutting down Socket server");
        doContinueAccept = false;
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

    public ClientConnection getNewClientConnection(Socket socket, int clientId) {
        return clientConnectionProvider.getNewClientConnection(socket, clientId);
    }
}
