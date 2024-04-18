package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

/**
 * Static utility class used to format requests and responses.
 * <p>
 * This class is used to format requests and responses by encrypting and encoding messages.
 * It is used by the client and server to format the messages.
 *
 * @author Ferfoui
 * @since 1.0
 */
@Deprecated(forRemoval = true)
public class RequestFormatting {

    /**
     * Encrypts a message and encodes it in Base64 format.
     *
     * @param clearMessage the message to encryptString and encode
     * @param key the key to use for encryption
     * @param algorithm the algorithm to use for encryption (for example, "RSA")
     * @return the encrypted and encoded message
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String encryptAndEncodeToString(String clearMessage, Key key, String algorithm) throws GeneralSecurityException {
        byte[] encryptedMessage = EncryptDecryptCipher.encryptString(clearMessage, key, algorithm);

        return new String(encode(encryptedMessage), StandardCharsets.ISO_8859_1);
    }

    /**
     * Decodes a Base64 encoded message and decrypts it.
     *
     * @param encryptedMessage the message to decode and decryptToString
     * @param key the key to use for decryption
     * @param algorithm the algorithm to use for decryption (for example, "RSA")
     * @return the decrypted message
     * @throws GeneralSecurityException if a security error occurs like an invalid key or algorithm
     */
    public static String decodeAndDecrypt(String encryptedMessage, Key key, String algorithm) throws GeneralSecurityException {
        byte[] decodedMessage = decode(encryptedMessage);

        return EncryptDecryptCipher.decryptToString(decodedMessage, key, algorithm);
    }

    /**
     * Encodes a byte array in Base64 format.
     *
     * @param bytesToEncode the bytes to encode
     * @return the encoded bytes
     */
    private static byte[] encode(byte[] bytesToEncode) {
        return Base64.getEncoder().encode(bytesToEncode);
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
