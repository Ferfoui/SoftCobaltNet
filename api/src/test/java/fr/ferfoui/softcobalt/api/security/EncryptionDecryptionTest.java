package fr.ferfoui.softcobalt.api.security;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.security.GeneralSecurityException;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EncryptionDecryptionTest {

    private static final String SAMPLE_TEXT = "Hello World!";
    public static final String ALGORITHM = ApiConstants.SecurityConstants.RSA_ALGORITHM;

    private AsymmetricKeysManager getKeysManager() {
        return new RsaKeysManager();
    }

    @Test
    public void testPublicEncryptionPrivateDecryption() throws GeneralSecurityException {
        // Generate the keys
        var keysManager = getKeysManager();
        keysManager.generateKeys();

        Key publicKey = keysManager.getPublicKey();
        Key privateKey = keysManager.getPrivateKey();

        // Encrypt the text with the public key and decrypt it with the private key
        byte[] encrypted = EncryptDecryptCipher.encrypt(SAMPLE_TEXT, publicKey, ALGORITHM);
        String decrypted = EncryptDecryptCipher.decrypt(encrypted, privateKey, ALGORITHM);

        assertEquals(SAMPLE_TEXT, decrypted);
    }

    @Test
    public void testPrivateEncryptionPublicDecryption() throws GeneralSecurityException {
        // This test is the same as the previous one but with the keys reversed
        var keysManager = getKeysManager();
        keysManager.generateKeys();

        Key publicKey = keysManager.getPublicKey();
        Key privateKey = keysManager.getPrivateKey();

        byte[] encrypted = EncryptDecryptCipher.encrypt(SAMPLE_TEXT, privateKey, ALGORITHM);
        String decrypted = EncryptDecryptCipher.decrypt(encrypted, publicKey, ALGORITHM);

        assertEquals(SAMPLE_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysPublicPrivate() throws GeneralSecurityException {
        // Generate two different key pairs for encryption and decryption
        var keysManager1 = getKeysManager();
        keysManager1.generateKeys();
        Key publicKey1 = keysManager1.getPublicKey();

        var keysManager2 = getKeysManager();
        keysManager2.generateKeys();
        Key privateKey2 = keysManager2.getPrivateKey();

        byte[] encrypted = EncryptDecryptCipher.encrypt(SAMPLE_TEXT, publicKey1, ALGORITHM);

        String decrypted;

        // Try to decrypt with the incorrect private key
        try {
            decrypted = EncryptDecryptCipher.decrypt(encrypted, privateKey2, ALGORITHM);
        } catch (BadPaddingException e) {
            return;
        }

        assertNotEquals(SAMPLE_TEXT, decrypted);
    }

    @Test
    public void testDifferentKeysPrivatePublic() throws GeneralSecurityException {
        // This test is the same as the previous one but with the keys reversed
        var keysManager1 = getKeysManager();
        keysManager1.generateKeys();
        Key privateKey1 = keysManager1.getPrivateKey();

        var keysManager2 = getKeysManager();
        keysManager2.generateKeys();
        Key publicKey2 = keysManager2.getPublicKey();

        byte[] encrypted = EncryptDecryptCipher.encrypt(SAMPLE_TEXT, privateKey1, ALGORITHM);

        String decrypted;

        // Try to decrypt with the incorrect public key
        try {
            decrypted = EncryptDecryptCipher.decrypt(encrypted, publicKey2, ALGORITHM);
        } catch (BadPaddingException e) {
            return;
        }

        assertNotEquals(SAMPLE_TEXT, decrypted);
    }
}
