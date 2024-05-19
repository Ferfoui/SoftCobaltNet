package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;

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
        return new DataRequest(header, body).getBytes();
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
