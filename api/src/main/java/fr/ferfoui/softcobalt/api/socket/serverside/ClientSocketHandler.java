package fr.ferfoui.softcobalt.api.socket.serverside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketHandler extends Thread {
    private final Socket clientSocket;
    private final int port;
    private final RequestProcessor serverLogic;

    /**
     * Constructor for the ClientSocketHandler
     * @param socket The socket to handle
     * @param port The port the socket is connected to
     * @param requestProcessor The logic to process the requests
     */
    public ClientSocketHandler(Socket socket, int port, RequestProcessor requestProcessor) {
        this.clientSocket = socket;
        this.port = port;
        this.serverLogic = requestProcessor;
    }

    /**
     * Run all the logic to handle the client socket
     */
    @Override
    public void run() {
        try {
            System.out.println("Client connected on port " + port);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            boolean continueListening = true;

            while (continueListening) {
                continueListening = serverLogic.processRequest(in, out);
                System.out.println(continueListening);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
        }
    }

    /**
     * Check if the socket is closed
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {
        return clientSocket.isClosed();
    }
}

