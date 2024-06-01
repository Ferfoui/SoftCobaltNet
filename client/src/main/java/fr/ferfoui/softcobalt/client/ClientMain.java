package fr.ferfoui.softcobalt.client;

import fr.ferfoui.softcobalt.api.network.CommunicationClient;
import fr.ferfoui.softcobalt.common.Constants;
import fr.ferfoui.softcobalt.common.file.FileCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientMain {

    private static final String SERVER_IP = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger("client");


    public static void main(String[] args) {

        /*FileCollector fileCollector = new FileCollector("");

        try {
            fileCollector.collectFiles();
        } catch (IOException e) {
            logger.error("Could not collect files", e);
        }

        logger.info("Files collected from directory: {}", fileCollector.getDirectory());
        fileCollector.getFilePaths().forEach(path -> logger.info("File: {}", path.toString()));
        */
        CommunicationClient communicationClient = new CommunicationClient(SERVER_IP, Constants.SERVER_PORT, logger);

        for (int i = 0; i < 10; i++) {
            String message = "Hello from client " + i;

            communicationClient.sendText(message);

            String response = new String(communicationClient.readData());
        
            logger.info("Received response: {}", response);
        }

        String message = "exit";

        communicationClient.sendText(message);
        communicationClient.closeCommunication();


    }

}