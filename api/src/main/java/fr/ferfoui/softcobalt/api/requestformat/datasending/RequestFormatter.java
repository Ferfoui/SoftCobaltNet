package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;

public interface RequestFormatter {

    /**
     * Create a request with a header and a body
     *
     * @param header the header of the request
     * @param body the body of the request
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
     * Create a request with a file body
     *
     * @param file the body of the request
     * @param fileName the name of the file
     * @return the request
     */
    byte[] createFileRequest(byte[] file, String fileName);
}
