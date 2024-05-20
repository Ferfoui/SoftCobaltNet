package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;
import fr.ferfoui.softcobalt.api.security.SecureExchanger;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Reader used to read data securely.
 * The data can be decrypted using a key.
 * The decryption is disabled by default.
 * The key must be set before enabling the decryption.
 * The decryption is done using the key and the algorithm associated with it.
 * The decryption is done using the {@link EncryptDecryptCipher#decrypt(byte[], Key, String)} method.
 */
@Deprecated
public class SecureDataReader implements SecureExchanger {

    private Key key;
    private String keyAlgorithm;
    private boolean isDecryptionEnabled = false;

    /**
     * Reads the data.
     * If the decryption is enabled, the data is decrypted using the key and the algorithm associated with it.
     *
     * @param data The data to read.
     * @return The data reader.
     * @throws NoSuchPaddingException    If the padding is not found.
     * @throws IllegalBlockSizeException If the block size is invalid.
     * @throws NoSuchAlgorithmException  If the algorithm is not found.
     * @throws BadPaddingException       If the padding is invalid.
     * @throws InvalidKeyException       If the key is invalid.
     */
    public DataReader readData(byte[] data)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (isDecryptionEnabled) {
            byte[] decryptedData = EncryptDecryptCipher.decrypt(data, key, keyAlgorithm);
            return new DataReader(decryptedData);
        } else {
            return new DataReader(data);
        }
    }

    /**
     * Retrieves the security key.
     *
     * @return The security key.
     */
    @Override
    public Key getKey() {
        return this.key;
    }

    /**
     * Sets the security key and its associated algorithm.
     *
     * @param key       The security key.
     * @param algorithm The algorithm associated with the key.
     */
    @Override
    public void setKey(@NotNull Key key, @NotNull String algorithm) {
        this.key = key;
        this.keyAlgorithm = algorithm;
    }

    /**
     * Retrieves the algorithm associated with the security key.
     *
     * @return The algorithm of the key.
     */
    @Override
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    /**
     * Enables or disables the decryption of the data.
     * The decryption is disabled by default, and the key must be set before enabling it.
     *
     * @param isEnable True to enable the decryption, false to disable it.
     */
    public void enableDecryption(boolean isEnable) {
        if (isEnable && key == null) {
            throw new IllegalStateException("The key must be set before enabling the decryption.");
        }
        this.isDecryptionEnabled = isEnable;
    }
}
