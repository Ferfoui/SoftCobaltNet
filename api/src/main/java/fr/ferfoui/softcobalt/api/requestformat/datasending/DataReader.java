package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;

import java.nio.charset.StandardCharsets;

public class DataReader implements RequestReader {

    private final byte[] data;
    private final int headerSize;

    public DataReader(byte[] data) {
        this.data = data;
        this.headerSize = calculateHeaderSize(data);
    }

    @Override
    public Header readHeader() {
        byte[] headerBytes = new byte[headerSize];
        System.arraycopy(data, 0, headerBytes, 0, headerSize);

        return new Header(headerBytes);
    }

    @Override
    public byte[] readBody() {
        byte[] body = new byte[data.length - headerSize];
        System.arraycopy(data, headerSize, body, 0, body.length);

        return body;
    }

    private int calculateHeaderSize(byte[] data) {
        byte[] headerSuffixBytes = ApiConstants.RequestFormatConstants.HEADER_SUFFIX.getBytes(StandardCharsets.UTF_8);
        int suffixIndex = indexOf(data, headerSuffixBytes);

        if (suffixIndex == -1) {
            throw new IllegalArgumentException("Invalid data: Header not found");
        }

        return suffixIndex + headerSuffixBytes.length;
    }

    private int indexOf(byte[] outerArray, byte[] smallerArray) {
        for (int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;

            for (int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }

            if (found) return i;
        }
        return -1;
    }
}
