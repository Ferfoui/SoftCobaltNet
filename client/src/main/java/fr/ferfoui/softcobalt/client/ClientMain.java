package fr.ferfoui.softcobalt.client;

import fr.ferfoui.softcobalt.api.requestformat.PublicKeySendingProtocol;
import fr.ferfoui.softcobalt.api.requestformat.RequestFormatting;
import fr.ferfoui.softcobalt.api.socket.clientside.ClientSocketManager;
import fr.ferfoui.softcobalt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class ClientMain {

    private static final String SERVER_IP = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);
    private static final ClientSocketManager clientSocketManager = new ClientSocketManager(logger);

    private static PublicKey publicKey = null;

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        clientSocketManager.startConnection(SERVER_IP, Constants.SERVER_PORT);

        for (int i = 0; i < 10; i++) {
            String response;

            if (publicKey == null) {
                response = clientSocketManager.sendMessage("Hello from client " + i);
            } else {
                String encryptedMessage = RequestFormatting.encryptAndEncode("Hello from client " + i, publicKey, "RSA");

                response = clientSocketManager.sendMessage(encryptedMessage);
            }

            if (PublicKeySendingProtocol.isPublicKeyMessage(response)) {
                try {
                    publicKey = decodePublicKeyInResponse(response);

                    String encryptedMessage = RequestFormatting.encryptAndEncode("First encrypted message with this key", publicKey, "RSA");

                    response = clientSocketManager.sendMessage(encryptedMessage);

                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }

            logger.info("Received response: {}", response);
        }

        String encryptedMessage = RequestFormatting.encryptAndEncode("exit", publicKey, "RSA");

        clientSocketManager.sendMessage(encryptedMessage);
    }

    private static PublicKey decodePublicKeyInResponse(String requestWithPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = PublicKeySendingProtocol.decodeReceivedPublicKey(requestWithPublicKey, "RSA");

        logger.info("Received public key: {}", publicKey);

        return publicKey;
    }
}