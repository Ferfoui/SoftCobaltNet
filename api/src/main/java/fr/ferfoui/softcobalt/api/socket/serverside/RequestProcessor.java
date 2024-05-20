package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;

import java.io.IOException;

/**
 * Interface used to process the requests from the client
 * <p>
 * This interface is used to process the requests from the client
 * and return if the server should continue listening for requests.
 * It is used by the ServerSocketManager to process the requests.
 *
 * @author Ferfoui
 * @since 1.0
 */
public interface RequestProcessor {

    /**
     * Initialize the data streams to use
     *
     * @throws IOException If the data streams cannot be initialized
     */
    void initializeDataStreams() throws IOException;

    /**
     * Process the request
     *
     * @param availableData The data available to process
     * @param logger        the logger to use
     */
    void processRequest(byte[] availableData, Logger logger);

    /**
     * Close the data streams
     *
     * @throws IOException If the data streams cannot be closed
     */
    void closeDataStreams() throws IOException;

    /**
     * Return if the server should continue listening for requests
     *
     * @return true if the server should continue listening for requests
     */
    boolean doContinueListening();
}
