package fr.ferfoui.softcobalt.api.security;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

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
     * @throws NoSuchPaddingException    if the padding is not available
     * @throws NoSuchAlgorithmException  if the algorithm is not available
     * @throws InvalidKeyException       if the key is invalid
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public static byte[] encrypt(byte[] bytesToEncrypt, @NotNull Key key, @NotNull String algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher encryptCipher = Cipher.getInstance(algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        return encryptCipher.doFinal(bytesToEncrypt);
    }

    /**
     * Encrypts a string using the given key and algorithm.
     *
     * @param clearText the string to encrypt
     * @param key       the key to use for encryption
     * @param algorithm the algorithm to use for encryption (for example, "RSA")
     * @return the encrypted string in bytes format
     * @throws NoSuchPaddingException    if the padding is not available
     * @throws NoSuchAlgorithmException  if the algorithm is not available
     * @throws InvalidKeyException       if the key is invalid
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public static byte[] encryptString(String clearText, Key key, String algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] clearTextBytes = clearText.getBytes();

        return encrypt(clearTextBytes, key, algorithm);
    }

    /**
     * Decrypts an encrypted byte array using the given key and algorithm.
     *
     * @param encryptedBytes the bytes to decrypt
     * @param key            the key to use for decryption
     * @param algorithm      the algorithm to use for decryption (for example, "RSA")
     * @return the decrypted bytes
     * @throws NoSuchPaddingException    if the padding is not available
     * @throws NoSuchAlgorithmException  if the algorithm is not available
     * @throws InvalidKeyException       if the key is invalid
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public static byte[] decrypt(byte[] encryptedBytes, Key key, String algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

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
     * @throws NoSuchPaddingException    if the padding is not available
     * @throws NoSuchAlgorithmException  if the algorithm is not available
     * @throws InvalidKeyException       if the key is invalid
     * @throws IllegalBlockSizeException if the block size is invalid
     * @throws BadPaddingException       if the padding is invalid
     */
    public static String decryptToString(byte[] encryptedTextBytes, Key key, String algorithm)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        byte[] decryptedTextBytes = decrypt(encryptedTextBytes, key, algorithm);

        return new String(decryptedTextBytes, StandardCharsets.UTF_8);
    }

}
