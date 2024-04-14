package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.PrintWriter;

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
     * Process the request and return true if the server should continue listening for requests
     * @param logger the logger to use
     * @return true if the server should continue listening for requests
     */
    boolean processRequest(Logger logger);
}
