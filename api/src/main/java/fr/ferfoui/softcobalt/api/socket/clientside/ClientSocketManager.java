package fr.ferfoui.softcobalt.api.socket.clientside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketManager {
    private Socket server;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Start the connection to the server
     * @param ip The IP of the server
     * @param port The port of the server
     * @throws IOException If the connection cannot be established
     */
    public void startConnection(String ip, int port) throws IOException {
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
        out.println(msg);
        return in.readLine();
    }

    /**
     * Stop the connection to the server
     * @throws IOException If the connection cannot be closed
     */
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        server.close();
    }
}
