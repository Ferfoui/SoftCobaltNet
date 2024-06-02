package fr.ferfoui.softcobalt.api.requestformat.request;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import org.apache.commons.lang3.SerializationUtils;

public record DataRequest(Header header, byte[] body) implements Request {

    /**
     * Returns the byte array corresponding to the request.
     *
     * @return The byte array of the request.
     */
    @Override
    public byte[] getBytes() {
        byte[] headerBytes = header.getHeaderBytes();
        byte[] request = new byte[headerBytes.length + body.length];

        System.arraycopy(headerBytes, 0, request, 0, headerBytes.length);
        System.arraycopy(body, 0, request, headerBytes.length, body.length);

        return request;
    }

    /**
     * Returns the deserialized body of the request.
     */
    @Override
    public Object getDeserializedBody() {
        return SerializationUtils.deserialize(body);
    }
}
