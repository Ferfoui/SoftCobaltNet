package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.PublicKeySendingProtocol;
import fr.ferfoui.softcobalt.api.requestformat.RequestFormatting;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import fr.ferfoui.softcobalt.api.socket.serverside.ClientSocketHandler;
import fr.ferfoui.softcobalt.api.socket.serverside.RequestProcessor;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import fr.ferfoui.softcobalt.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager = new ServerSocketManager("server-socket");

    private final AsymmetricKeysManager rsaKeysManager = new RsaKeysManager();

    private final Logger logger = LoggerFactory.getLogger("SocketServerThread");

    SocketServerThread() {
        super("SocketServerThread");
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
            serverSocketManager.start(Constants.SERVER_PORT, new NiceRequestProcessor());
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


    private class NiceRequestProcessor implements RequestProcessor {
        @Override
        public boolean processRequest(BufferedReader in, PrintWriter out, int clientId, Logger logger) {
            String request = null;
            try {
                request = in.readLine();
                logger.info("Received request: {}", request);

                if (!Utils.isBase64(request)) {
                    logger.info("Request is not encoded, sending public key");
                    sendPublicKey(out);
                } else {
                    String decryptedRequest = decryptRequest(request);

                    logger.info("Decoded request: {}", decryptedRequest);

                    String encryptedResponse = RequestFormatting.encryptAndEncode("Server response for: '" + decryptedRequest + "'", rsaKeysManager.getPrivateKey(), ApiConstants.SecurityConstants.RSA_ALGORITHM);
                    logger.info("Sending encrypted response: {}", encryptedResponse);
                    out.println(encryptedResponse);
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

        private void sendPublicKey(PrintWriter out) {
            out.println(PublicKeySendingProtocol.createPublicKeyMessage(rsaKeysManager.getPublicKey()));
        }
    }
}
