package fr.ferfoui.softcobalt.server;

import fr.ferfoui.softcobalt.api.requestformat.PublicKeySendingProtocol;
import fr.ferfoui.softcobalt.api.requestformat.RequestFormatting;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import fr.ferfoui.softcobalt.api.socket.serverside.ClientSocketHandler;
import fr.ferfoui.softcobalt.api.socket.serverside.RequestProcessor;
import fr.ferfoui.softcobalt.api.socket.serverside.ServerSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import fr.ferfoui.softcobalt.common.Utils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SocketServerThread extends Thread {

    private final ServerSocketManager serverSocketManager = new ServerSocketManager("server-socket");

    private final RsaKeysManager rsaKeysManager = new RsaKeysManager();

    SocketServerThread() {
        super("SocketServerThread");
    }

    @Override
    public void run() {

        try {
            rsaKeysManager.generateKeys();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

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

                if (!checkRequestIsEncoded(request)) {
                    logger.info("Request is not encoded, sending public key");
                    sendPublicKey(out);
                } else {
                    String decryptedRequest = decryptRequest(request);

                    logger.info("Decoded request: {}", decryptedRequest);
                    out.println("Server response for: '" + decryptedRequest + "'");
                    request = decryptedRequest;
                }

            } catch (IOException e) {
                logger.error("Error reading request", e);
            } catch (GeneralSecurityException e) {
                logger.error("Error decrypting request", e);
            }
            return request != null && !request.equals("exit");
        }

        private boolean checkRequestIsEncoded(String request) {
            return Utils.isBase64(request);
        }

        private String decryptRequest(String request) throws GeneralSecurityException {
            return RequestFormatting.decodeAndDecrypt(request, rsaKeysManager.getPrivateKey(), rsaKeysManager.RSA_ALGORITHM);
        }

        private void sendPublicKey(PrintWriter out) {
            out.println(PublicKeySendingProtocol.createPublicKeyMessage(rsaKeysManager.getPublicKey()));
        }
    }
}
