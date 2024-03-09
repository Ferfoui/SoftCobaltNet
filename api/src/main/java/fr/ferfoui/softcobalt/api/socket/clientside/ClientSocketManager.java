package fr.ferfoui.softcobalt.api.socket.clientside;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketManager {
    private Socket server;
    private PrintWriter out;
    private BufferedReader in;
    private final Logger logger;

    public ClientSocketManager(@Nullable Logger logger) {
        this.logger = logger == null ? LoggerFactory.getLogger("client-socket-manager") : logger;
    }

    /**
     * Start the connection to the server
     * @param ip The IP of the server
     * @param port The port of the server
     * @throws IOException If the connection cannot be established
     */
    public void startConnection(String ip, int port) throws IOException {
        logger.info("Starting connection to {}:{}", ip, port);
        server = new Socket(ip, port);
        out = new PrintWriter(server.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    /**
     * Send a message to the server
     * @param msg The message to send
     * @return The response from the server
     * @throws IOException If the message cannot be sent
     */
    public String sendMessage(String msg) throws IOException {
        logger.info("Sending message: {}", msg);
        out.println(msg);
        return in.readLine();
    }

    /**
     * Stop the connection to the server
     * @throws IOException If the connection cannot be closed
     */
    public void stopConnection() throws IOException {
        logger.info("Stopping connection");
        in.close();
        out.close();
        server.close();
    }
}
