package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import fr.ferfoui.softcobalt.api.socket.serverside.clientconnection.ClientConnection;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager;

    private final Logger logger = LoggerFactory.getLogger("SocketServerThread");

    SocketServerThread() {
        super("SocketServerThread");
        serverSocketManager = new ServerSocketManager("server-socket", StringProcessServer::new);
    }

    @Override
    public void run() {
        // Start the server
        try {
            serverSocketManager.start(Constants.SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void stopServer() {
        logger.info("Stopping server");
        try {
            serverSocketManager.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClientHandlersTerminated() {
        logger.debug("Checking if client handlers are terminated");
        return serverSocketManager.isClientHandlersTerminated();
    }


    private class StringProcessServer extends ClientConnection {

        private final DataFormatter dataFormatter = new DataFormatter();

        private boolean doContinueListening = true;

        /**
         * Constructor for the ClientConnection.
         *
         * @param socket   The socket to handle
         * @param clientId The id of the client
         */
        public StringProcessServer(Socket socket, long clientId) {
            super(socket, clientId);
        }

        /**
         * Process the request
         *
         * @param availableData The data available to process
         * @param logger        The logger to use
         */
        @Override
        public void processRequest(byte[] availableData, Logger logger) {
            String requestString = null;
            try {
                logger.info("Received data: {}", availableData);
                DataReader dataReader = new DataReader(availableData);

                DataRequest request = dataReader.getRequests().get(0);
                requestString = new String(request.body(), ApiConstants.RequestFormatConstants.DEFAULT_CHARSET);

                logger.info("Received request: '{}'", requestString);

            } catch (IOException e) {
                logger.error("Error reading request", e);
            }

            try {
                String response = "Server response for: '" + requestString + "'";
                sendData(dataFormatter.createStringRequest(response));
            } catch (IOException e) {
                logger.error("Error sending response", e);
            }

            if (requestString != null && requestString.contains("exit")) {
                serverSocketManager.stopClientHandlers();
            }

            doContinueListening = requestString != null && !requestString.contains("exit");
        }

        /**
         * Return if the server should continue listening for requests
         *
         * @return true if the server should continue listening for requests
         */
        @Override
        public boolean doContinueListening() {
            return this.doContinueListening;
        }
    }
}
