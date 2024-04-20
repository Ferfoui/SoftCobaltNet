package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private final ArrayList<ClientSocketHandler> clientHandlers = new ArrayList<>();
    ClientConnectionProvider clientConnectionProvider;
    private ServerSocket serverSocket;
    private boolean doContinueAccept;

    /**
     * Constructor for the ServerSocketManager
     *
     * @param name                     The name of the logger to use
     * @param clientConnectionProvider The client connection provider to use. This is used to create a new client connection
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

    /**
     * Accept connections from clients
     *
     * @throws IOException If an error occurs while accepting a connection
     */
    private void acceptConnections() throws IOException {
        doContinueAccept = true;

        long clientId = 0;
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

    /**
     * Join all client handlers
     *
     * @throws InterruptedException If the thread is interrupted while joining
     */
    public synchronized void joinClientHandlers() throws InterruptedException {
        for (ClientSocketHandler clientHandler : clientHandlers) {
            clientHandler.join();
        }
    }

    /**
     * Get a new client connection
     *
     * @param socket   The socket to use
     * @param clientId The client id
     * @return The new client connection
     */
    public ClientConnection getNewClientConnection(Socket socket, long clientId) {
        return clientConnectionProvider.getNewClientConnection(socket, clientId);
    }
}
