package fr.ferfoui.softcobalt.api.socket.serverside;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ClientConnection implements RequestProcessor {

    protected Socket socket;
    protected int clientId;
    protected DataInputStream in;
    protected DataOutputStream out;

    /**
     * Constructor for the ClientConnection.
     *
     * @param socket   The socket to handle
     * @param clientId The id of the client
     */
    public ClientConnection(@NotNull Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data streams", e);
        }
    }

    public void sendBytes(byte[] bytesToSend) throws IOException {
        out.writeInt(bytesToSend.length);
        out.write(bytesToSend);
        out.flush();
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
