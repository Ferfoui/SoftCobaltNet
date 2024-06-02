package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;
import fr.ferfoui.softcobalt.api.requestformat.instruction.SendingFileInstructions;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import fr.ferfoui.softcobalt.api.socket.serverside.clientconnection.ClientConnection;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

        private String directoryPath;

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

            logger.info("Received data: {}", availableData);
            DataReader dataReader = new DataReader(availableData);

            DataRequest request = dataReader.getRequests().get(0);

            try {

                Header header = request.header();
                if (HeaderPrincipalKeyword.INSTRUCTIONS.isKeywordMatching(header.getPrincipalKeyword())) {

                    SendingFileInstructions instructions = (SendingFileInstructions) request.getDeserializedBody();
                    directoryPath = instructions.getDirectoryPath();

                    logger.info("Received instructions: '{}'", instructions.getUUID().toString());
                    sendData(dataFormatter.createAcceptRequest(instructions.getUUID()));

                    return;
                }

                requestString = new String(request.body(), ApiConstants.RequestFormatConstants.DEFAULT_CHARSET);


                if (HeaderPrincipalKeyword.FILE.isKeywordMatching(header.getPrincipalKeyword())) {
                    logger.info("Received file: '{}'", requestString);


                    Path filePath = Path.of(directoryPath, header.getSecondaryKeywords().get(ApiConstants.RequestFormatConstants.FILENAME_KEYWORD));

                    Files.write(filePath, request.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                } else {

                    String response = "Server response for: '" + requestString + "'";
                    sendData(dataFormatter.createStringRequest(response));
                }

            } catch (IOException e) {
                logger.error("Error handling the request response", e);
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
