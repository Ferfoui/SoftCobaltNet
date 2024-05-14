package fr.ferfoui.softcobalt.api.network;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.RequestFormatter;
import fr.ferfoui.softcobalt.api.socket.clientside.ClientNetworkConnection;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileSenderClient {

    private final ClientNetworkConnection<byte[]> connection;
    private final Logger logger;
    private final RequestFormatter requestFormatter;

    public FileSenderClient(ClientNetworkConnection<byte[]> connection, @Nullable Logger logger) {
        this.connection = connection;
        this.logger = (logger == null) ?
                LoggerFactory.getLogger("file-sender-client") : logger;
        this.requestFormatter = new DataFormatter();
    }

    public void sendFile(String ip, int port, File file) {
        try {
            connection.startConnection(ip, port);
            connection.sendData(requestFormatter.createFileRequest(Files.readAllBytes(file.toPath()), file.getName()));
            connection.closeConnection();
        } catch (IOException e) {
            logger.error("Error while sending the file", e);
        }

    }

}
