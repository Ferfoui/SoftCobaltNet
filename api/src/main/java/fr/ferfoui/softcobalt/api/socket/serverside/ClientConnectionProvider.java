package fr.ferfoui.softcobalt.api.socket.serverside;

import java.net.Socket;

public interface ClientConnectionProvider {

    ClientConnection getNewClientConnection(Socket socket, int clientId);

}
