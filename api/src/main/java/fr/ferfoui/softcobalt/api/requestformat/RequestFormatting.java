package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

/**
 * Class used to format requests and responses.
 */
public class RequestFormatting {

    /**
     * Encrypts a message and encodes it in Base64 format.
     *
     * @param clearMessage the message to encrypt and encode
     * @param key the key to use for encryption
     * @param algorithm the algorithm to use for encryption (e.g. "RSA/ECB/PKCS1Padding")
     * @return the encrypted and encoded message
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String encryptAndEncode(String clearMessage, Key key, String algorithm) throws GeneralSecurityException {
        byte[] encryptedMessage = EncryptDecryptCipher.encrypt(clearMessage, key, algorithm);

        return encode(encryptedMessage);
    }

    /**
     * Decodes a Base64 encoded message and decrypts it.
     *
     * @param encryptedMessage the message to decode and decrypt
     * @param key the key to use for decryption
     * @param algorithm the algorithm to use for decryption (e.g. "RSA/ECB/PKCS1Padding")
     * @return the decrypted message
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String decodeAndDecrypt(String encryptedMessage, Key key, String algorithm) throws GeneralSecurityException {
        byte[] decodedMessage = decode(encryptedMessage);

        return EncryptDecryptCipher.decrypt(decodedMessage, key, algorithm);
    }

    /**
     * Encodes a byte array in Base64 format.
     *
     * @param bytesToEncode the bytes to encode
     * @return the encoded bytes
     */
    private static String encode(byte[] bytesToEncode) {
        return Base64.getEncoder().encodeToString(bytesToEncode);
    }

    /**
     * Decodes a Base64 encoded string.
     *
     * @param stringToDecode the string to decode
     * @return the decoded bytes
     */
    private static byte[] decode(String stringToDecode) {
        return Base64.getDecoder().decode(stringToDecode);
    }

}