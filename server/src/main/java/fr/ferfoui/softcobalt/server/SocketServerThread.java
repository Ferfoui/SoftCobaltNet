package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import fr.ferfoui.softcobalt.api.socket.serverside.ClientConnection;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import fr.ferfoui.softcobalt.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager;

    private final AsymmetricKeysManager rsaKeysManager = new RsaKeysManager();

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

    public void stopServer() {
        try {
            serverSocketManager.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClientHandlersTerminated() {
        return serverSocketManager.isClientHandlersTerminated();
    }


    private static class StringProcessServer extends ClientConnection {
        /**
         * Constructor for the ClientConnection.
         *
         * @param socket   The socket to handle
         * @param clientId The id of the client
         */
        public StringProcessServer(Socket socket, long clientId) {
            super(socket, clientId);
        }

        private final DataFormatter dataFormatter = new DataFormatter();

        @Override
        public boolean processRequest(byte[] availableData, Logger logger) {
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

            return requestString != null && !requestString.contains("exit");
        }
    }
}
