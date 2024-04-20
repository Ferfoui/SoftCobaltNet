package fr.ferfoui.softcobalt.api.socket.serverside;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ClientConnection implements RequestProcessor {

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

    public void initializeDataStreams() throws IOException {
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Receive bytes from the client
     * @return The bytes received
     * @throws IOException If the bytes cannot be received
     */
    protected byte[] readBytes() throws IOException {
        byte[] bytesReceived = new byte[in.available()];
        in.readFully(bytesReceived);
        return bytesReceived;
    }

    /**
     * Send bytes to the client
     * @param bytesToSend The bytes to send
     * @throws IOException If the bytes cannot be sent
     */
    protected void sendBytes(byte[] bytesToSend) throws IOException {
        out.write(bytesToSend);
    }

    /**
     * Close the connection
     *
     * @throws IOException If the connection cannot be closed
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
