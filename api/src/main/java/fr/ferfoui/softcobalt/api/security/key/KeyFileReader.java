package fr.ferfoui.softcobalt.api.security.key;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class used to read public and private keys from files.
 *
 * @see KeyFileSaver
 */
public class KeyFileReader {

    /**
     * Reads a public key from a file.
     *
     * @param publicKeyFile the file containing the public key
     * @param algorithm the algorithm used to generate the key (e.g. "RSA/ECB/PKCS1Padding")
     * @return the public key
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static PublicKey readPublicKeyFromFile(File publicKeyFile, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        return KeyFactory.getInstance(algorithm).generatePublic(publicKeySpec);
    }

    /**
     * Reads a private key from a file.
     *
     * @param privateKeyFile the file containing the private key
     * @param algorithm the algorithm used to generate the key (e.g. "RSA/ECB/PKCS1Padding")
     * @return the private key
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static PrivateKey readPrivateKeyFromFile(File privateKeyFile, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        return KeyFactory.getInstance(algorithm).generatePrivate(privateKeySpec);
    }
}
