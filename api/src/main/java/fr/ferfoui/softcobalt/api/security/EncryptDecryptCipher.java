package fr.ferfoui.softcobalt.api.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * Static class used to encrypt and decrypt strings using a key and an algorithm.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class EncryptDecryptCipher {

    /**
     * Encrypts a string using the given key and algorithm.
     *
     * @param clearText the string to encrypt
     * @param key the key to use for encryption
     * @param algorithm the algorithm to use for encryption (e.g. "RSA/ECB/PKCS1Padding")
     * @return the encrypted string in bytes format
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static byte[] encrypt(String clearText, Key key, String algorithm)
            throws GeneralSecurityException {

        Cipher encryptCipher = Cipher.getInstance(algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] clearTextBytes = clearText.getBytes();

        return encryptCipher.doFinal(clearTextBytes);
    }

    /**
     * Decrypts a Base64 encoded string using the given key and algorithm.
     *
     * @param encryptedTextBytes bytes of the text to decrypt
     * @param key the key to use for decryption
     * @param algorithm the algorithm to use for decryption (e.g. "RSA/ECB/PKCS1Padding")
     * @return the decrypted string
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String decrypt(byte[] encryptedTextBytes, Key key, String algorithm)
            throws GeneralSecurityException {

        Cipher decryptCipher = Cipher.getInstance(algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedTextBytes = decryptCipher.doFinal(encryptedTextBytes);

        return new String(decryptedTextBytes, StandardCharsets.UTF_8);
    }
}
