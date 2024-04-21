package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;

import java.security.Key;

public class DataFormatter implements RequestFormatter {

    /**
     * Create a request with a header and a body
     *
     * @param header the header of the request
     * @param body   the body of the request
     * @return the request
     */
    @Override
    public byte[] createRequest(Header header, byte[] body) {
        byte[] headerBytes = header.getHeaderBytes();
        byte[] request = new byte[headerBytes.length + body.length];

        System.arraycopy(headerBytes, 0, request, 0, headerBytes.length);
        System.arraycopy(body, 0, request, headerBytes.length, body.length);

        return request;
    }

    /**
     * Create a request with a string body
     *
     * @param body the body of the request
     * @return the request
     */
    @Override
    public byte[] createStringRequest(String body) {
        Header header = new Header(HeaderPrincipalKeyword.STRING);
        return createRequest(header, body.getBytes());
    }

    /**
     * Create a request with a public key body
     *
     * @param publicKey    the key that will be sent
     * @param keyAlgorithm the algorithm of the key
     * @return the request
     */
    @Override
    public byte[] createPublicKeyRequest(Key publicKey, String keyAlgorithm) {
        Header header = new Header(HeaderPrincipalKeyword.PUBLIC_KEY);
        header.addSecondaryKeywords(RequestFormatConstants.KEY_ALGORITHM_KEYWORD + "=" + keyAlgorithm);

        return createRequest(header, publicKey.getEncoded());
    }

    /**
     * Create a request with a file body
     *
     * @param file     the file that will be sent
     * @param fileName the name of the file
     * @return the request
     */
    @Override
    public byte[] createFileRequest(byte[] file, String fileName) {
        Header header = new Header(HeaderPrincipalKeyword.FILE);
        header.addSecondaryKeywords(RequestFormatConstants.FILENAME_KEYWORD + "=" + fileName);
        return createRequest(header, file);
    }


}