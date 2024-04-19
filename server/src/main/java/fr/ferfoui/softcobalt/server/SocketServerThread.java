package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.PublicKeySendingUtils;
import fr.ferfoui.softcobalt.api.requestformat.RequestFormatting;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import fr.ferfoui.softcobalt.api.socket.serverside.ClientConnection;
import fr.ferfoui.softcobalt.api.socket.serverside.ClientSocketHandler;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import fr.ferfoui.softcobalt.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager;

    private final AsymmetricKeysManager rsaKeysManager = new RsaKeysManager();

    private final Logger logger = LoggerFactory.getLogger("SocketServerThread");

    SocketServerThread() {
        super("SocketServerThread");
        serverSocketManager = new ServerSocketManager("server-socket", NiceRequestProcessor::new);
    }

    @Override
    public void run() {

        File privateKeyFile = new File(Constants.PRIVATE_KEY_FILE);
        File publicKeyFile = new File(Constants.PUBLIC_KEY_FILE);

        // If the keys don't exist, generate them and save them to files or if they exist, load them from files
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            try {
                logger.info("Generating RSA keys");
                rsaKeysManager.generateKeys();
                rsaKeysManager.saveKeysToFiles(publicKeyFile, privateKeyFile);
            } catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                logger.info("Loading RSA keys from files");
                rsaKeysManager.loadKeysFromFiles(publicKeyFile, privateKeyFile);
            } catch (IOException | GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }

        // Start the server
        try {
            serverSocketManager.start(Constants.SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            serverSocketManager.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClientSocketHandler> getClientHandlers() {
        return serverSocketManager.getClientHandlers();
    }


    private class NiceRequestProcessor extends ClientConnection {
        /**
         * Constructor for the ClientConnection.
         *
         * @param socket   The socket to handle
         * @param clientId The id of the client
         */
        public NiceRequestProcessor(Socket socket, int clientId) {
            super(socket, clientId);
        }

        @Override
        public boolean processRequest(Logger logger) {
            String request = null;
            try {
                request = in.readUTF();
                logger.info("Received request: {}", request);

                if (!Utils.isBase64(request)) {
                    logger.info("Request is not encoded, sending public key");
                    sendPublicKey();
                } else {
                    String decryptedRequest = decryptRequest(request);

                    logger.info("Decoded request: {}", decryptedRequest);

                    String encryptedResponse = RequestFormatting.encryptAndEncodeToString("Server response for: '" + decryptedRequest + "'", rsaKeysManager.getPrivateKey(), ApiConstants.SecurityConstants.RSA_ALGORITHM);
                    logger.info("Sending encrypted response: {}", encryptedResponse);
                    out.writeUTF(encryptedResponse);
                    request = decryptedRequest;
                }

            } catch (IOException e) {
                logger.error("Error reading request", e);
            } catch (GeneralSecurityException e) {
                logger.error("Error decrypting request", e);
            }
            return request != null && !request.equals("exit");
        }

        private String decryptRequest(String request) throws GeneralSecurityException {
            return RequestFormatting.decodeAndDecrypt(request, rsaKeysManager.getPrivateKey(), ApiConstants.SecurityConstants.RSA_ALGORITHM);
        }

        private void sendPublicKey() throws IOException {
            out.writeUTF(PublicKeySendingUtils.createPublicKeyMessage(rsaKeysManager.getPublicKey()));
        }
    }
}
