package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecureDataFormatterTest {

    @Test
    public void testCreateStringRequest() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        AsymmetricKeysManager keysManager = new RsaKeysManager();
        keysManager.generateKeys();

        SecureDataFormatter secureFormatter = new SecureDataFormatter();
        secureFormatter.setKey(keysManager.getPublicKey(), keysManager.getAlgorithm());
        secureFormatter.enableEncryption(true);

        String SAMPLE_TEXT = "Hello World!";
        byte[] encryptedRequest = secureFormatter.createStringRequest(SAMPLE_TEXT);

        byte[] decryptedRequest = EncryptDecryptCipher.decrypt(
                encryptedRequest, keysManager.getPrivateKey(), keysManager.getAlgorithm()
        );

        DataReader dataReader = new DataReader(decryptedRequest);

        Header header = dataReader.readHeader();

        String body = new String(dataReader.readBody());

        assertEquals(SAMPLE_TEXT, body);
        assertNotNull(header.getHeaderBytes());
    }
}
