package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;

public class DataFormatter implements RequestFormatter{

    @Override
    public byte[] createRequest(Header header, byte[] body) {
        byte[] headerBytes = header.getHeaderBytes();
        byte[] request = new byte[headerBytes.length + body.length];

        System.arraycopy(headerBytes, 0, request, 0, headerBytes.length);
        System.arraycopy(body, 0, request, headerBytes.length, body.length);

        return request;
    }

    @Override
    public byte[] createStringRequest(String body) {
        Header header = new Header(HeaderPrincipalKeyword.STRING);
        return createRequest(header, body.getBytes());
    }

    @Override
    public byte[] createFileRequest(byte[] file, String fileName) {
        Header header = new Header(HeaderPrincipalKeyword.FILE);
        header.addSecondaryKeywords(RequestFormatConstants.FILENAME_KEYWORD + "=" + fileName);
        return createRequest(header, file);
    }
}
