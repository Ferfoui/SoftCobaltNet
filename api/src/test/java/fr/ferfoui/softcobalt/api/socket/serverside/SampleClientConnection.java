package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class SampleClientConnection extends ClientConnection {
    public static final String EXIT_COMMAND = "exit";

    private final DataFormatter formatter;

    /**
     * Constructor for the ClientConnection.
     *
     * @param socket   The socket to handle
     * @param clientId The id of the client
     */
    public SampleClientConnection(Socket socket, long clientId) {
        super(socket, clientId);
        formatter = new DataFormatter();
    }

    /**
     * Process the request and return true if the server should continue listening for requests
     *
     * @param logger the logger to use
     * @return true if the server should continue listening for requests
     */
    @Override
    public boolean processRequest(Logger logger) {
        String body;
        try {
            byte[] bytesReceived = readBytes();
            if (bytesReceived.length == 0) {
                return true;
            }
            DataReader reader = new DataReader(bytesReceived);
            body = readStringBody(reader);
            logger.info("Received request: {}", body);

            String response = "Server response for: '" + body + "'";
            logger.info("Sending response: {}", response);
            sendResponse(response);

        } catch (IOException e) {
            logger.error("Error reading request", e);
            return false;
        }

        return !body.isEmpty() && !body.equals(EXIT_COMMAND);
    }

    private String readStringBody(DataReader reader) {
        return new String(reader.readBody());
    }

    private void sendResponse(String response) throws IOException {
        byte[] bytesToSend = formatter.createStringRequest(response);
        sendBytes(bytesToSend);
    }
}
