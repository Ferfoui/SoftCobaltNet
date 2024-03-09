package fr.ferfoui.softcobalt.api.cryptography;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Class to manage RSA keys
 */
public class RsaKeysManager {

    private final String RSA_ALGORITHM = "RSA";

    private KeyPair keyPair;

    /**
     * Generate a new pair of RSA keys
     * @throws NoSuchAlgorithmException If the algorithm is not found
     */
    public void generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
    }

    /**
     * Read the keys from the given files
     * @param publicKeyFile The file containing the public key
     * @param privateKeyFile The file containing the private key
     * @throws IOException If an error occurs while reading the files
     * @throws InvalidKeySpecException If the keys are invalid
     * @throws NoSuchAlgorithmException If the algorithm is not found
     */
    public void readKeysFromFiles(File publicKeyFile, File privateKeyFile)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        keyPair = new KeyPair(
                KeyFileReader.readPublicKeyFromFile(publicKeyFile, RSA_ALGORITHM),
                KeyFileReader.readPrivateKeyFromFile(privateKeyFile, RSA_ALGORITHM)
        );
    }

    /**
     * Get the public key
     * @return The public key
     */
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * Get the private key
     * @return The private key
     */
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    /**
     * Set the key pair
     * @param keyPair The key pair to set
     */
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
