package fr.ferfoui.softcobalt.api.socket.clientside;

import fr.ferfoui.softcobalt.api.socket.DataQueueSocketManager;
import fr.ferfoui.softcobalt.api.socket.NetworkConnection;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Class used to manage the client socket connection.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ClientSocketManager extends DataQueueSocketManager implements NetworkConnection<byte[]> {

    private final Thread queueHandlerThread = new Thread(getQueueHandlerRunnable());
    private DataInputStream in;
    private DataOutputStream out;

    public ClientSocketManager(@Nullable Logger logger) {
        super(null, logger);
    }

    /**
     * This method is returning the logger to use.
     *
     * @param logger The logger to use, if null, a new logger will be created
     * @return The logger to use
     */
    @Override
    protected Logger getLogger(@Nullable Logger logger) {
        return (logger == null) ?
                LoggerFactory.getLogger("client-socket-manager") : logger;
    }

    /**
     * Start the connection to the server
     *
     * @param ip   The IP of the server
     * @param port The port of the server
     * @throws IOException If the connection cannot be established
     */
    public void startConnection(String ip, int port) throws IOException {
        logger.info("Starting connection to {}:{}", ip, port);
        socket = new Socket(ip, port);
        queueHandlerThread.start();
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Send bytes to the server
     *
     * @param bytesToSend The bytes to send
     * @throws IOException If the bytes cannot be sent
     */
    @Override
    public void sendData(byte[] bytesToSend) throws IOException {
        logger.info("Sending bytes: {}", new String(bytesToSend, StandardCharsets.UTF_8));
        out.write(bytesToSend);
    }

    /**
     * Receive bytes from the server
     *
     * @return The received bytes
     * @throws IOException If the bytes cannot be received
     */
    @Override
    public byte[] readData() throws IOException {
        byte[] bytesReceived = new byte[in.available()];
        in.readFully(bytesReceived);
        logger.info("Received bytes: {}", new String(bytesReceived, StandardCharsets.UTF_8));
        return bytesReceived;
    }

    /**
     * Stop the connection to the server
     *
     * @throws IOException If the connection cannot be closed
     */
    @Override
    public void close() throws IOException {
        logger.info("Stopping connection");
        queueHandlerThread.interrupt();
        in.close();
        out.close();
        socket.close();
    }
}
