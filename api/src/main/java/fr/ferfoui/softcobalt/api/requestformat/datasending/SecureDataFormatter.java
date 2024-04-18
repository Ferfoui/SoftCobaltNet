package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;
import fr.ferfoui.softcobalt.api.security.SecureExchanger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SecureDataFormatter extends DataFormatter implements SecureExchanger {

    private Key key;
    private String keyAlgorithm;
    private boolean isEncryptionEnabled = false;

    @Override
    public byte[] createRequest(Header header, byte[] body) {

        byte[] clearRequest = super.createRequest(header, body);

        if (!isEncryptionEnabled)
            return clearRequest;

        return encryptRequest(clearRequest);
    }

    private byte[] encryptRequest(byte[] clearRequest) {
        try {
            return EncryptDecryptCipher.encrypt(clearRequest, key, keyAlgorithm);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException("Error while encrypting the request", e.getCause());
        }
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public void setKey(Key key, String algorithm) {
        this.key = key;
        this.keyAlgorithm = algorithm;
    }

    @Override
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void enableEncryption(boolean isEnable) {
        this.isEncryptionEnabled = isEnable;
    }
}
