package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PublicKeySendingTest {

    public static final String SAMPLE_TEXT = "Hello World!";

    private static final String ALGORITHM = ApiConstants.SecurityConstants.RSA_ALGORITHM;

    private AsymmetricKeysManager getKeysManager() {
        return new RsaKeysManager();
    }

    @Test
    public void testEncodeDecodePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        var keysManager = getKeysManager();
        keysManager.generateKeys();

        PublicKey publicKey = keysManager.getPublicKey();

        String publicKeyMessage = PublicKeySendingProtocol.createPublicKeyMessage(publicKey);
        PublicKey decodedPublicKey = PublicKeySendingProtocol.decodeReceivedPublicKey(publicKeyMessage, ALGORITHM);

        assertEquals(publicKey, decodedPublicKey);
    }

    @Test
    public void testDecodeNonPublicKeyMessage() throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            PublicKeySendingProtocol.decodeReceivedPublicKey(SAMPLE_TEXT, ALGORITHM);
            fail("The program didn't send error after trying to decode a non public key message");
        } catch (IllegalArgumentException ignored) {
            // It is working right
        }
    }

}
