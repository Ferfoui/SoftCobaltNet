package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;

public interface RequestFormatter {

    /**
     * Create a request with a header and a body, see {@link fr.ferfoui.softcobalt.api.requestformat.request.DataRequest}
     *
     * @param header the header of the request
     * @param body   the body of the request
     * @return the request
     */
    byte[] createRequest(Header header, byte[] body);

    /**
     * Create a request with a {@link java.lang.String} body
     *
     * @param body the body of the request
     * @return the request
     */
    byte[] createStringRequest(String body);

    /**
     * Create a request with a file body
     *
     * @param file     the file that will be sent
     * @param fileName the name of the file
     * @return the request
     */
    byte[] createFileRequest(byte[] file, String fileName);

    /**
     * Create a request with an {@link fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions} body
     *
     * @param instructions the instructions that will be sent
     * @return the request
     */
    byte[] createInstructionRequest(Instructions instructions);
}
