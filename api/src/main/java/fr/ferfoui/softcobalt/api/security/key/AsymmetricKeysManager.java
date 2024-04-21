package fr.ferfoui.softcobalt.api.security.key;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Class to manage asymmetric cryptographic keys
 *
 * @author Ferfoui
 * @since 1.0
 */
public interface AsymmetricKeysManager {

    /**
     * Generate a new pair of keys
     */
    void generateKeys();

    /**
     * Read the keys from the given files
     *
     * @param publicKeyFile  The file containing the public key
     * @param privateKeyFile The file containing the private key
     * @throws IOException              If an error occurs while reading the files
     * @throws InvalidKeySpecException  If the keys are invalid
     * @throws NoSuchAlgorithmException If the algorithm is not found
     */
    void loadKeysFromFiles(File publicKeyFile, File privateKeyFile)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException;

    /**
     * Save the keys into the given files
     *
     * @param publicKeyFile  The file to save the public key into
     * @param privateKeyFile The file to save the private key into
     * @throws IOException If an error occurs while writing the files
     */
    void saveKeysToFiles(File publicKeyFile, File privateKeyFile) throws IOException;

    /**
     * Get the public key
     *
     * @return the public key
     */
    PublicKey getPublicKey();

    /**
     * Get the private key
     *
     * @return the private key
     */
    PrivateKey getPrivateKey();

    /**
     * Get the algorithm used to generate the keys
     *
     * @return the algorithm
     */
    String getAlgorithm();

    /**
     * Set a new key pair
     *
     * @param keyPair The key pair to set
     */
    void setKeyPair(KeyPair keyPair);
}
