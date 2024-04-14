package fr.ferfoui.softcobalt.api.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * Static utility class used to encrypt and decrypt bytes or strings using a key and an algorithm.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class EncryptDecryptCipher {

    /**
     * Encrypts a byte array using the given key and algorithm.
     *
     * @param bytesToEncrypt the bytes to encrypt
     * @param key            the key to use for encryption
     * @param algorithm      the algorithm to use for encryption (for example, "RSA")
     * @return the encrypted bytes
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    private static byte[] encryptBytes(byte[] bytesToEncrypt, Key key, String algorithm)
            throws GeneralSecurityException {
        Cipher encryptCipher = Cipher.getInstance(algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        return encryptCipher.doFinal(bytesToEncrypt);
    }

    /**
     * Encrypts a string using the given key and algorithm.
     *
     * @param clearText the string to encryptBytes
     * @param key       the key to use for encryption
     * @param algorithm the algorithm to use for encryption (for example, "RSA")
     * @return the encrypted string in bytes format
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static byte[] encryptString(String clearText, Key key, String algorithm)
            throws GeneralSecurityException {

        byte[] clearTextBytes = clearText.getBytes();

        return encryptBytes(clearTextBytes, key, algorithm);
    }

    /**
     * Decrypts an encrypted byte array using the given key and algorithm.
     *
     * @param encryptedBytes the bytes to decrypt
     * @param key            the key to use for decryption
     * @param algorithm      the algorithm to use for decryption (for example, "RSA")
     * @return the decrypted bytes
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static byte[] decryptToBytes(byte[] encryptedBytes, Key key, String algorithm)
            throws GeneralSecurityException {

        Cipher decryptCipher = Cipher.getInstance(algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        return decryptCipher.doFinal(encryptedBytes);
    }

    /**
     * Decrypts an encrypted string using the given key and algorithm.
     *
     * @param encryptedTextBytes bytes of the text to decrypt
     * @param key                the key to use for decryption
     * @param algorithm          the algorithm to use for decryption (for example, "RSA")
     * @return the decrypted string
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String decryptToString(byte[] encryptedTextBytes, Key key, String algorithm)
            throws GeneralSecurityException {

        byte[] decryptedTextBytes = decryptToBytes(encryptedTextBytes, key, algorithm);

        return new String(decryptedTextBytes, StandardCharsets.UTF_8);
    }

}
