package fr.ferfoui.softcobalt.api.socket.clientside;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Class used to manage the client socket connection.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ClientSocketManager {
    private Socket server;
    private DataInputStream in;
    private DataOutputStream out;
    private final Logger logger;

    public ClientSocketManager(@Nullable Logger logger) {
        this.logger = (logger == null) ?
                LoggerFactory.getLogger("client-socket-manager") : logger;
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
        in = new DataInputStream(new BufferedInputStream(server.getInputStream()));
        out = new DataOutputStream(server.getOutputStream());
    }

    /**
     * Send a message to the server
     * @param msg The message to send
     * @return The response from the server
     * @throws IOException If the message cannot be sent
     */
    @Deprecated
    public String sendMessage(String msg) throws IOException {
        logger.info("Sending message: {}", msg);
        out.writeUTF(msg);
        return in.readUTF();
    }

    /**
     * Send bytes to the server
     * @param bytesToSend The bytes to send
     * @throws IOException If the bytes cannot be sent
     */
    public void sendBytes(byte[] bytesToSend) throws IOException {
        logger.info("Sending bytes: {}", new String(bytesToSend, StandardCharsets.UTF_8));
        out.write(bytesToSend);
    }

    /**
     * Receive bytes from the server
     * @return The bytes received
     * @throws IOException If the bytes cannot be received
     */
    public byte[] readBytes() throws IOException {
        byte[] bytesReceived = new byte[in.available()];
        in.readFully(bytesReceived);
        logger.info("Received bytes: {}", new String(bytesReceived, StandardCharsets.UTF_8));
        return bytesReceived;
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
