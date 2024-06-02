package fr.ferfoui.softcobalt.api.requestformat.request;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;

public interface Request {

    /**
     * Returns the header of the request.
     *
     * @return The header of the request.
     */
    Header header();

    /**
     * Returns the body of the request.
     *
     * @return The body of the request.
     */
    byte[] body();

    /**
     * Returns the byte array corresponding to the request.
     *
     * @return The byte array of the request.
     */
    byte[] getBytes();

    /**
     * Returns the deserialized body of the request.
     */
    Object getDeserializedBody();

}
