package fr.ferfoui.softcobalt.api.network;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.datasending.RequestFormatter;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;
import fr.ferfoui.softcobalt.api.requestformat.instruction.SendingFileInstructions;
import fr.ferfoui.softcobalt.api.socket.clientside.SSLClientSocketManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class is responsible for managing the communication with the server.
 * It uses SSL for secure communication.
 */
public class CommunicationClient {

    private final SSLClientSocketManager connection;
    private final Logger logger;
    private final RequestFormatter requestFormatter;
    private final String ip;
    private final int port;

    private Thread queueHandlerThread;

    /**
     * Constructor for the CommunicationClient
     *
     * @param ip     The ip of the server to connect to
     * @param port   The port of the server to connect to
     * @param logger The logger to use, if null, a new logger will be created
     */
    public CommunicationClient(String ip, int port, @Nullable Logger logger) {
        this.ip = ip;
        this.port = port;
        this.connection = new SSLClientSocketManager(logger);

        this.logger = (logger == null) ?
                LoggerFactory.getLogger("file-sender-client") : logger;

        this.requestFormatter = new DataFormatter();
    }

    /**
     * Starts the communication with the server.
     * If the client is not already connected, it will attempt to connect.
     */
    public void startCommunication() {
        if (!connection.isConnected()) {
            try {
                connection.startConnection(ip, port);
            } catch (IOException e) {
                logger.error("Error while starting the communication", e);
            }

            queueHandlerThread = new Thread(connection.getQueueHandlerRunnable());
        }
    }

    /**
     * Closes the communication with the server.
     * If the client is connected, it will attempt to close the connection.
     */
    public void closeCommunication() {
        if (connection.isConnected()) {

            try {
                connection.closeConnection();
            } catch (IOException e) {
                logger.error("Error while closing the communication", e);
            }

            queueHandlerThread.interrupt();
        }
    }

    /**
     * Sends a {@link String} to the server
     *
     * @param text The text to send
     */
    public void sendText(String text) {
        startCommunication();

        try {
            connection.sendData(requestFormatter.createStringRequest(text));
        } catch (IOException e) {
            logger.error("Error while sending the text", e);
        }
    }

    /**
     * Sends multiple files to the server
     *
     * @param directoryPath The path of the directory where the files are stored on the server
     * @param files         The files to send
     */
    public void sendMultipleFiles(String directoryPath, File... files) {
        startCommunication();

        Instructions fileInstructions = new SendingFileInstructions(directoryPath);

        try {
            connection.sendData(requestFormatter.createInstructionsRequest(fileInstructions));
        } catch (IOException e) {
            logger.error("Error while sending the file instructions", e);
        }

        try {
            byte[] response = connection.waitUntilDataAvailable();
            DataReader dataReader = new DataReader(response);
            if (HeaderPrincipalKeyword.NO_PROBLEM.isKeywordMatching(dataReader.getRequests().get(0).header().getPrincipalKeyword())) {
                logger.error("Error while sending the file instructions");
                return;
            }
        } catch (IOException e) {
            logger.error("Error while waiting for the server to be ready", e);
        }

        try {
            for (File file : files) {
                byte[] fileRequest = requestFormatter.createFileRequest(Files.readAllBytes(file.toPath()), file.getName(), fileInstructions.getUUID());
                connection.sendData(fileRequest);
            }
        } catch (IOException e) {
            logger.error("Error while sending the files", e);
        }
    }

    /**
     * Reads the data from the server
     *
     * @return The data read
     */
    public byte[] readData() {
        startCommunication();

        try {
            byte[] data = connection.waitUntilDataAvailable();

            DataReader dataReader = new DataReader(data);

            return dataReader.getRequests().get(0).body();

        } catch (IOException e) {
            logger.error("Error while reading the data", e);
        }

        return null;
    }

}
