package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface RequestProcessor {
    /**
     * Process the request and return true if the server should continue listening for requests
     * @param in the input stream
     * @param out the output stream
     * @param logger the logger to use
     * @return true if the server should continue listening for requests
     */
    boolean processRequest (BufferedReader in, PrintWriter out, int clientId, Logger logger);
}
