package fr.ferfoui.softcobalt.client;

import fr.ferfoui.softcobalt.api.network.CommunicationClient;
import fr.ferfoui.softcobalt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

    private static final String SERVER_IP = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);


    public static void main(String[] args) {

        CommunicationClient communicationClient = new CommunicationClient(SERVER_IP, Constants.SERVER_PORT, logger);

        for (int i = 0; i < 10; i++) {
            String response;

            String message = "Hello from client " + i;

            communicationClient.sendText(message);
        }

        String message = "exit";

        communicationClient.sendText(message);
        communicationClient.closeCommunication();
    }

}