package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;

import java.security.Key;

public interface RequestFormatter {

    /**
     * Create a request with a header and a body
     *
     * @param header the header of the request
     * @param body   the body of the request
     * @return the request
     */
    byte[] createRequest(Header header, byte[] body);

    /**
     * Create a request with a string body
     *
     * @param body the body of the request
     * @return the request
     */
    byte[] createStringRequest(String body);

    /**
     * Create a request with a public key body
     *
     * @param publicKey    the key that will be sent
     * @param keyAlgorithm the algorithm of the key
     * @return the request
     */
    byte[] createPublicKeyRequest(Key publicKey, String keyAlgorithm);

    /**
     * Create a request with a file body
     *
     * @param file     the file that will be sent
     * @param fileName the name of the file
     * @return the request
     */
    byte[] createFileRequest(byte[] file, String fileName);
}
