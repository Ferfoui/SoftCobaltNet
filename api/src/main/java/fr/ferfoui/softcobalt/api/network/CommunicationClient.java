package fr.ferfoui.softcobalt.api.network;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.datasending.RequestFormatter;
import fr.ferfoui.softcobalt.api.socket.clientside.SSLClientSocketManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CommunicationClient {

    private final SSLClientSocketManager connection;
    private final Logger logger;
    private final RequestFormatter requestFormatter;
    private final String ip;
    private final int port;

    private Thread queueHandlerThread;

    public CommunicationClient(String ip, int port, @Nullable Logger logger) {
        this.ip = ip;
        this.port = port;
        this.connection = new SSLClientSocketManager(logger);

        this.logger = (logger == null) ?
                LoggerFactory.getLogger("file-sender-client") : logger;

        this.requestFormatter = new DataFormatter();
    }

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

    public void sendText(String text) {
        startCommunication();

        try {
            connection.sendData(requestFormatter.createStringRequest(text));
        } catch (IOException e) {
            logger.error("Error while sending the text", e);
        }
    }

    public void sendFile(File file) {
        startCommunication();

        try {
            connection.sendData(requestFormatter.createFileRequest(Files.readAllBytes(file.toPath()), file.getName()));
        } catch (IOException e) {
            logger.error("Error while sending the file", e);
        }
    }

    public byte[] readData() {
        startCommunication();

        byte[] data;
        try {
            data = connection.waitUntilDataAvailable();

            DataReader dataReader = new DataReader(data);

            return dataReader.getRequests().get(0).body();

        } catch (IOException e) {
            logger.error("Error while reading the data", e);
        }

        return null;
    }

}
