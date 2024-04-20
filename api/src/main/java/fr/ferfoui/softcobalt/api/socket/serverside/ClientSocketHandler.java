package fr.ferfoui.softcobalt.api.socket.serverside;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Class used to handle the client socket connection.
 * <p>
 * This class is used to handle the client socket connection and process the requests.
 * It is used by the ServerSocketManager to handle the client connections.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ClientSocketHandler extends Thread {
    private final Socket clientSocket;
    private final ClientConnection clientConnection;
    private final long clientId;
    private final Logger logger;

    private boolean doContinueListening = false;

    /**
     * Constructor for the ClientSocketHandler
     *
     * @param socket           The socket to handle
     * @param clientConnection The client connection to use
     * @param clientId         The id of the client
     * @param logger           The logger to use, if null, a new logger will be created
     */
    public ClientSocketHandler(Socket socket, ClientConnection clientConnection, long clientId, @Nullable Logger logger) {
        super("client-socket-handler-" + clientId);
        this.clientSocket = socket;
        this.clientId = clientId;
        this.logger = getLogger(logger);
        this.clientConnection = clientConnection;
    }

    /**
     * Get the logger to use
     *
     * @param logger The logger to use, if null, a new logger will be created
     * @return The logger to use
     */
    private Logger getLogger(@Nullable Logger logger) {
        return (logger == null) ? LoggerFactory.getLogger(this.getName())
                : LoggerFactory.getLogger(logger.getName() + "-" + this.getName());
    }

    /**
     * Run all the logic to handle the client socket
     */
    @Override
    public void run() {
        try {
            logger.info("Client-{} connected on port {}", clientId, clientSocket.getPort());
            clientConnection.initializeDataStreams();
            doContinueListening = true;

            while (doContinueListening) {
                doContinueListening = clientConnection.processRequest(logger);
                logger.debug("The server should continue listening: {}", doContinueListening);
            }

            clientConnection.close();
            logger.info("Client-{} disconnected", clientId);
        } catch (IOException e) {
            logger.error("Exception caught when trying to listen on port {} or listening for a connection", clientSocket.getPort(), e);
        }
    }

    /**
     * Stop the client socket handler
     */
    public void stopHandler() {
        doContinueListening = false;
    }

    /**
     * Check if the socket is closed
     *
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {
        return clientSocket.isClosed();
    }
}

