package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.socket.NetworkConnection;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ClientConnection implements RequestProcessor, NetworkConnection<byte[]> {

    protected Socket socket;
    protected long clientId;
    protected DataInputStream in;
    protected DataOutputStream out;

    /**
     * Constructor for the ClientConnection.
     *
     * @param socket   The socket to handle
     * @param clientId The id of the client
     */
    public ClientConnection(@NotNull Socket socket, long clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    /**
     * Initialize the data streams
     *
     * @throws IOException If the data streams cannot be initialized
     */
    @Override
    public void initializeDataStreams() throws IOException {
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Send bytes to the client
     *
     * @param bytesToSend The bytes to send
     * @throws IOException If the bytes cannot be sent
     */
    @Override
    public void sendData(byte[] bytesToSend) throws IOException {
        out.write(bytesToSend);
    }

    /**
     * Receive bytes from the client
     *
     * @return The received bytes
     * @throws IOException If the bytes cannot be received
     */
    @Override
    public byte[] readData() throws IOException {
        byte[] bytesReceived = new byte[in.available()];
        in.readFully(bytesReceived);
        return bytesReceived;
    }

    /**
     * Close the data streams
     *
     * @throws IOException If the data streams cannot be closed
     */
    @Override
    public void closeDataStreams() throws IOException {
        in.close();
        out.close();
    }

    /**
     * Close the connection
     *
     * @throws IOException If the connection cannot be closed
     */
    @Override
    public void closeConnection() throws IOException {
        closeDataStreams();
        socket.close();
    }

    /**
     * Check if the connection is active
     *
     * @return True if the connection is active, false otherwise
     */
    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

}
