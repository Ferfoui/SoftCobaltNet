package fr.ferfoui.softcobalt.api.socket.clientside;

import fr.ferfoui.softcobalt.api.socket.NetworkConnection;

import java.io.IOException;

/**
 * Interface used to manage a client network connection
 *
 * @param <T> The type of data to send and receive
 */
public interface ClientNetworkConnection<T> extends NetworkConnection<T> {

    /**
     * Start the connection to the server
     *
     * @param ip   The IP of the server
     * @param port The port of the server
     * @throws IOException If the connection cannot be established
     */
    void startConnection(String ip, int port) throws IOException;

}
