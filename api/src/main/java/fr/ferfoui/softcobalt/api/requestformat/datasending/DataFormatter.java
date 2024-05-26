package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import org.apache.commons.lang3.SerializationUtils;

public class DataFormatter implements RequestFormatter {

    /**
     * Create a request with a header and a body, see {@link fr.ferfoui.softcobalt.api.requestformat.request.DataRequest}
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
     * Create a request with a {@link java.lang.String} body
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

    /**
     * Create a request with {@link fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions} body
     *
     * @param instructions the instructions that will be sent
     * @return the request
     */
    @Override
    public byte[] createInstructionsRequest(Instructions instructions) {
        Header header = new Header(HeaderPrincipalKeyword.INSTRUCTIONS);
        return createRequest(header, SerializationUtils.serialize(instructions));
    }

}
