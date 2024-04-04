package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestFormattingTest {

    private static final String SAMPLE_TEXT = "Hello World!";
    public static final String ALGORITHM = ApiConstants.SecurityConstants.RSA_ALGORITHM;

    private AsymmetricKeysManager getKeysManager() {
        return new RsaKeysManager();
    }

    @Test
    public void testSecureEncodingDecodingPublicPrivate() throws GeneralSecurityException {
        var keysManager = getKeysManager();
        keysManager.generateKeys();

        Key publicKey = keysManager.getPublicKey();
        Key privateKey = keysManager.getPrivateKey();

        // Encrypt the text with the public key and decrypt it with the private key
        String encoded = RequestFormatting.encryptAndEncode(SAMPLE_TEXT, publicKey, ALGORITHM);
        String decoded = RequestFormatting.decodeAndDecrypt(encoded, privateKey, ALGORITHM);

        assertEquals(SAMPLE_TEXT, decoded);
    }

    @Test
    public void testSecureEncodingDecodingPrivatePublic() throws GeneralSecurityException {
        // This test is the same as the previous one but with the keys reversed
        var keysManager = getKeysManager();
        keysManager.generateKeys();

        Key publicKey = keysManager.getPublicKey();
        Key privateKey = keysManager.getPrivateKey();

        String encoded = RequestFormatting.encryptAndEncode(SAMPLE_TEXT, privateKey, ALGORITHM);
        String decoded = RequestFormatting.decodeAndDecrypt(encoded, publicKey, ALGORITHM);

        assertEquals(SAMPLE_TEXT, decoded);
    }
}
