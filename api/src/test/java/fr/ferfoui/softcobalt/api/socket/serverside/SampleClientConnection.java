package fr.ferfoui.softcobalt.api.socket.serverside;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class SampleClientConnection extends ClientConnection {
    /**
     * Constructor for the ClientConnection.
     *
     * @param socket   The socket to handle
     * @param clientId The id of the client
     */
    public SampleClientConnection(Socket socket, int clientId) {
        super(socket, clientId);
    }

    /**
     * Process the request and return true if the server should continue listening for requests
     *
     * @param logger the logger to use
     * @return true if the server should continue listening for requests
     */
    @Override
    public boolean processRequest(Logger logger) {
        String request;
        try {
            request = in.readUTF();
            logger.info("Received request: {}", request);
            String response = "Server response for: '" + request + "'";
            logger.info("Sending response: {}", response);
            out.writeUTF(response);

        } catch (IOException e) {
            logger.error("Error reading request", e);
            return false;
        }

        return !request.isEmpty() && !request.equals("exit");
    }
}
