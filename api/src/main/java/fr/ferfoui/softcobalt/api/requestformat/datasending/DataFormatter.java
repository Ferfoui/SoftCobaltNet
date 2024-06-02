package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderPrincipalKeyword;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
     * Create a request with a {@link UUID} body,
     * this is used to send the UUID of a request that has been accepted
     *
     * @param uuid the UUID of the request
     * @return the request
     */
    @Override
    public byte[] createAcceptRequest(UUID uuid) {
        Header header = new Header(HeaderPrincipalKeyword.NO_PROBLEM);
        return createRequest(header, SerializationUtils.serialize(uuid));
    }

    /**
     * Create a request with a file body
     *
     * @param file     the file that will be sent
     * @param fileName the name of the file
     * @param uuid     the UUID of the file, which can be null
     * @return the request
     */
    @Override
    public byte[] createFileRequest(byte[] file, String fileName, @Nullable UUID uuid) {
        Header header = new Header(HeaderPrincipalKeyword.FILE);
        header.addSecondaryKeywords(RequestFormatConstants.FILENAME_KEYWORD + "=" + fileName);

        String uuidString = (uuid == null) ? "null" : uuid.toString();
        header.addSecondaryKeywords(RequestFormatConstants.UUID_KEYWORD + "=" + uuidString);

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
