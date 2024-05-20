package fr.ferfoui.softcobalt.api.socket.serverside;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.socket.serverside.thread.ServerThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class used to manage the server socket connection.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ServerSocketManager {

    private final ExecutorService clientHandlersExecutor = Executors.newCachedThreadPool(new ServerThreadFactory());
    private final SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

    private final Logger logger;
    private final ClientConnectionProvider clientConnectionProvider;
    private SSLServerSocket serverSocket;
    private boolean doContinueAccept;

    /**
     * Constructor for the ServerSocketManager
     *
     * @param name                     The name of the logger to use
     * @param clientConnectionProvider The client connection provider to use. This is used to create a new client connection
     */
    public ServerSocketManager(String name, ClientConnectionProvider clientConnectionProvider) {
        logger = LoggerFactory.getLogger(name);
        this.clientConnectionProvider = clientConnectionProvider;
    }

    /**
     * Start the server on the given port
     *
     * @param port The port to start the server on
     * @throws IOException If the server socket cannot be created
     */
    public synchronized void start(int port) throws IOException {
        logger.info("Starting server on port: {}", port);

        // TODO:
        //  Check how to use a certificate :
        //  https://docs.oracle.com/cd/E54932_01/doc.705/e54936/cssg_create_ssl_cert.htm#CSVSG178
        serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
        serverSocket.setEnabledProtocols(ApiConstants.SecurityConstants.SECURITY_PROTOCOLS);
        serverSocket.setEnabledCipherSuites(ApiConstants.SecurityConstants.CIPHER_SUITES);

        acceptConnections();
    }

    /**
     * Accept connections from clients
     *
     * @throws IOException If an error occurs while accepting a connection
     */
    private void acceptConnections() throws IOException {
        doContinueAccept = true;

        long clientId = 0;
        while (doContinueAccept) {
            try {
                Socket connectionSocket = serverSocket.accept();
                logger.info("Accepted connection from: {}", connectionSocket.getInetAddress());

                long finalClientId = clientId++;
                ClientSocketHandler clientSocketHandler = new ClientSocketHandler(connectionSocket,
                        getNewClientConnection(connectionSocket, finalClientId), finalClientId, null);

                clientHandlersExecutor.submit(clientSocketHandler);
                clientHandlersExecutor.submit(clientSocketHandler.getQueueHandlerRunnable());

            } catch (SocketException e) {
                // The Socket was closed while accept() was waiting, break the loop
                if (!doContinueAccept) {
                    break;
                } else {
                    logger.error("An error occurred while accepting a connection", e);
                }
            }
        }
    }

    /**
     * Return if the client handlers are terminated
     *
     * @return true if the client handlers are terminated
     */
    public boolean isClientHandlersTerminated() {
        return clientHandlersExecutor.isTerminated();
    }

    /**
     * Stop the server
     *
     * @throws IOException If the server socket cannot be closed
     */
    public void stop() throws IOException {
        logger.info("Shutting down Socket server");
        doContinueAccept = false;
        stopClientHandlers();
        serverSocket.close();
    }

    /**
     * Stop all client handlers
     */
    public void stopClientHandlers() {
        logger.info("Shutting down client handlers");
        clientHandlersExecutor.shutdown();
    }

    public void stopClientHandlersNow() {
        logger.info("Shutting down client handlers now");
    }

    /**
     * Get a new client connection
     *
     * @param socket   The socket to use
     * @param clientId The client id
     * @return The new client connection
     */
    public ClientConnection getNewClientConnection(Socket socket, long clientId) {
        return clientConnectionProvider.getNewClientConnection(socket, clientId);
    }
}
