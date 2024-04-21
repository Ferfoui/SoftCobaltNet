package fr.ferfoui.softcobalt.api.requestformat;

import fr.ferfoui.softcobalt.api.ApiConstants;
import org.jetbrains.annotations.NotNull;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Protocol used to encode and decode public keys.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class PublicKeySendingUtils {
    public static final String PUBLIC_KEY_SPLITTER = ApiConstants.RequestFormatConstants.REQUEST_SPLITTER;

    public static final String PUBLIC_KEY_PREFIX = ApiConstants.RequestFormatConstants.PUBLIC_KEY_PREFIX;
    public static final String PUBLIC_KEY_SUFFIX = ApiConstants.RequestFormatConstants.PUBLIC_KEY_SUFFIX;

    /**
     * Converts a public key to a byte array.
     *
     * @param publicKey the public key
     * @return the byte array containing the public key
     */
    public static byte[] publicKeyToBytes(@NotNull PublicKey publicKey) {
        return publicKey.getEncoded();
    }

    /**
     * Converts a public key to a byte array.
     *
     * @param publicKey the public key
     * @param algorithm the algorithm to use for the key factory
     * @return the byte array containing the public key
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static byte[] publicKeyToBytes(@NotNull PublicKey publicKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        EncodedKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, EncodedKeySpec.class);
        return publicKeySpec.getEncoded();
    }

    /**
     * Decodes a public key from a byte array.
     *
     * @param publicKeyBytes the byte array containing the public key
     * @return the public key
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static PublicKey decodeReceivedPublicKey(byte[] publicKeyBytes, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * Creates a public key message from a public key.
     *
     * @param publicKey the public key
     * @return the public key message in the format "public_key:::encoded_public_key:::end_public_key"
     */
    @Deprecated(forRemoval = true)
    public static String createPublicKeyMessage(@NotNull PublicKey publicKey) {

        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        return PUBLIC_KEY_PREFIX + encodedPublicKey + PUBLIC_KEY_SUFFIX;
    }

    /**
     * Decodes a public key from a string.
     *
     * @param receivedKeyMsg the string containing the encoded public key
     * @return the public key
     * @throws IllegalArgumentException if the string does not contain a public key
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    @Deprecated(forRemoval = true)
    public static PublicKey decodeReceivedStringPublicKey(@NotNull String receivedKeyMsg, String algorithm)
            throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {

        if (!isPublicKeyMessage(receivedKeyMsg)) {
            throw new IllegalArgumentException("The message does not match the public key message format");
        }

        String publicKeyString = receivedKeyMsg.split(PUBLIC_KEY_SPLITTER)[1];

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * Checks if a message is a public key message.
     *
     * @param msg the message to check
     * @return true if the message is a public key message, false otherwise
     */
    @Deprecated(forRemoval = true)
    public static boolean isPublicKeyMessage(String msg) {
        return msg != null && msg.startsWith(PUBLIC_KEY_PREFIX) && msg.endsWith(PUBLIC_KEY_SUFFIX);
    }

}