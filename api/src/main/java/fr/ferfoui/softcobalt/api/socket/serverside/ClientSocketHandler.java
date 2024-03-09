package fr.ferfoui.softcobalt.api.socket.serverside;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketHandler extends Thread {
    private final Socket clientSocket;
    private final RequestProcessor serverLogic;
    private final Logger logger;

    /**
     * Constructor for the ClientSocketHandler
     * @param socket The socket to handle
     * @param requestProcessor The logic to process the requests
     * @param logger The logger to use, if null, a new logger will be created
     */
    public ClientSocketHandler(Socket socket, RequestProcessor requestProcessor, @Nullable Logger logger) {
        this.clientSocket = socket;
        this.serverLogic = requestProcessor;
        this.logger = (logger == null)
                ? LoggerFactory.getLogger("client-socket-handler " + this.getName()) : logger;
    }

    /**
     * Run all the logic to handle the client socket
     */
    @Override
    public void run() {
        try {
            logger.info("Client connected on port {}", clientSocket.getPort());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            boolean continueListening = true;

            while (continueListening) {
                continueListening = serverLogic.processRequest(in, out, logger);
                logger.debug("The server should continue listening: {}", continueListening);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            logger.error("Exception caught when trying to listen on port "
                    + clientSocket.getPort() + " or listening for a connection", e);
        }
    }

    /**
     * Check if the socket is closed
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {
        return clientSocket.isClosed();
    }
}

