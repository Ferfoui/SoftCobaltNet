package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
     * Create a request with a {@link java.util.UUID} body
     *
     * @param uuid the UUID of the request
     * @return the request
     */
    byte[] createAcceptRequest(UUID uuid);

    /**
     * Create a request with a file body
     *
     * @param file     the file that will be sent
     * @param fileName the name of the file
     * @param uuid     the UUID of the file, which can be null
     * @return the request
     */
    byte[] createFileRequest(byte[] file, String fileName, @Nullable UUID uuid);

    /**
     * Create a request with an {@link fr.ferfoui.softcobalt.api.requestformat.instruction.Instructions} body
     *
     * @param instructions the instructions that will be sent
     * @return the request
     */
    byte[] createInstructionsRequest(Instructions instructions);
}
