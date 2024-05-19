package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.socket.DataQueueSocketManager;
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
public class ClientSocketHandler extends DataQueueSocketManager implements Runnable {

    private final RequestProcessor clientConnection;
    private final long clientId;
    private boolean doContinueListening = false;

    /**
     * Constructor for the ClientSocketHandler
     *
     * @param socket           The socket to handle
     * @param clientConnection The client connection to use
     * @param clientId         The id of the client
     * @param logger           The logger to use, if null, a new logger will be created
     */
    public ClientSocketHandler(Socket socket, RequestProcessor clientConnection, long clientId, @Nullable Logger logger) {
        super(socket, logger);
        this.clientId = clientId;
        this.clientConnection = clientConnection;
    }

    /**
     * Get the logger to use
     *
     * @param logger The logger to use, if null, a new logger will be created
     * @return The logger to use
     */
    @Override
    protected Logger getLogger(@Nullable Logger logger) {
        return (logger == null) ? LoggerFactory.getLogger("client-socket-handler-" + clientId)
                : LoggerFactory.getLogger(logger.getName() + "-" + "client-socket-handler-" + clientId);
    }

    /**
     * Run all the logic to handle the client socket
     */
    @Override
    public void run() {
        logger.info("Client-{} connected on port {}", clientId, socket.getPort());
        try {
            clientConnection.initializeDataStreams();
            doContinueListening = true;

            while (doContinueListening) {
                byte[] availableData = waitUntilDataAvailable();
                doContinueListening = clientConnection.processRequest(availableData, logger);
                logger.debug("The server should continue listening: {}", doContinueListening);
            }

            clientConnection.closeDataStreams();
            logger.info("Client-{} disconnected", clientId);
        } catch (IOException e) {
            logger.error("Exception caught when trying to listen on port {} or listening for a connection", socket.getPort(), e);
        }
    }

    /**
     * Stop the client socket handler
     */
    public void stopHandler() {
        doContinueListening = false;
    }

}

