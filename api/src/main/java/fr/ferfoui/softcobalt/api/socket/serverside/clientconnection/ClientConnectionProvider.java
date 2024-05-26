package fr.ferfoui.softcobalt.api.socket.serverside.clientconnection;

import java.net.Socket;

/**
 * Interface used to provide a new client connection
 * <p>
 * This is used to create a new client connection when a client connects to the server
 */
public interface ClientConnectionProvider {

    /**
     * Get a new client connection
     *
     * @param socket   The socket of the client
     * @param clientId The id of the client
     * @return The new client connection
     */
    ClientConnection getNewClientConnection(Socket socket, long clientId);

}
