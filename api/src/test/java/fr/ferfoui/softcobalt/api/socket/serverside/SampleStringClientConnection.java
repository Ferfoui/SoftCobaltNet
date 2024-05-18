package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class SampleStringClientConnection extends ClientConnection {
    public static final String EXIT_COMMAND = "exit";

    private final DataFormatter formatter;

    /**
     * Constructor for the ClientConnection.
     *
     * @param socket   The socket to handle
     * @param clientId The id of the client
     */
    public SampleStringClientConnection(Socket socket, long clientId) {
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
    public boolean processRequest(byte[] availableBytes, Logger logger) {
        String body;
        logger.info("Processing request from client-{}", clientId);
        try {
            if (availableBytes.length == 0) {
                return true;
            }
            DataReader reader = new DataReader(availableBytes);
            body = readStringBody(reader.getRequests().get(0));
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

    /**
     * Close the connection
     *
     * @throws IOException If the connection cannot be closed
     */
    @Override
    public void close() throws IOException {
        socket.close();
    }

    private String readStringBody(DataRequest request) {
        return new String(request.body());
    }

    private void sendResponse(String response) throws IOException {
        byte[] bytesToSend = formatter.createStringRequest(response);
        sendData(bytesToSend);
    }
}
