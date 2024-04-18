package fr.ferfoui.softcobalt.api.security;

import java.security.Key;

/**
 * The SecureExchanger interface provides a contract for managing a security key.
 * It allows getting, setting, and knowing the algorithm of the key.
 */
public interface SecureExchanger {

    /**
     * Retrieves the security key.
     *
     * @return The security key.
     */
    Key getKey();

    /**
     * Sets the security key and its associated algorithm.
     *
     * @param key The security key.
     * @param algorithm The algorithm associated with the key.
     */
    void setKey(Key key, String algorithm);

    /**
     * Retrieves the algorithm associated with the security key.
     *
     * @return The algorithm of the key.
     */
    String getKeyAlgorithm();
}
