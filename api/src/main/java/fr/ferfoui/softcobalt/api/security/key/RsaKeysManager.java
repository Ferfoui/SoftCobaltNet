package fr.ferfoui.softcobalt.api.security.key;

import fr.ferfoui.softcobalt.api.ApiConstants.SecurityConstants;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Class to manage RSA keys
 *
 * @author Ferfoui
 * @since 1.0
 */
public class RsaKeysManager implements AsymmetricKeysManager {

    private KeyPair keyPair;

    /**
     * Generate a new pair of RSA keys
     *
     * @throws NoSuchAlgorithmException If the algorithm is not found
     * @since 1.0
     */
    @Override
    public void generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(SecurityConstants.RSA_ALGORITHM);
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
    }

    /**
     * Read the keys from the given files
     *
     * @param publicKeyFile The file containing the public key
     * @param privateKeyFile The file containing the private key
     * @throws IOException If an error occurs while reading the files
     * @throws InvalidKeySpecException If the keys are invalid
     * @throws NoSuchAlgorithmException If the algorithm is not found
     * @see KeyFileReader
     * @since 1.0
     */
    @Override
    public void loadKeysFromFiles(File publicKeyFile, File privateKeyFile)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        keyPair = new KeyPair(
                KeyFileReader.readPublicKeyFromFile(publicKeyFile, SecurityConstants.RSA_ALGORITHM),
                KeyFileReader.readPrivateKeyFromFile(privateKeyFile, SecurityConstants.RSA_ALGORITHM)
        );
    }

    /**
     * Save the keys into the given files
     *
     * @param publicKeyFile The file to save the public key into
     * @param privateKeyFile The file to save the private key into
     * @throws IOException If an error occurs while writing the files
     * @see KeyFileSaver
     * @since 1.0
     */
    @Override
    public void saveKeysToFiles(File publicKeyFile, File privateKeyFile) throws IOException {
        KeyFileSaver.saveKeyToFile(keyPair.getPublic(), publicKeyFile);
        KeyFileSaver.saveKeyToFile(keyPair.getPrivate(), privateKeyFile);
    }

    @Override
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    @Override
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

}
