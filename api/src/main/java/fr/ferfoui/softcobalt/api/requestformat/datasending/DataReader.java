package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    private final List<DataRequest> requests;

    /**
     * Creates a new data reader with a byte array.
     *
     * @param data The byte array containing the data.
     */
    public DataReader(byte[] data) {
        this.requests = splitRequests(data);
    }

    public List<DataRequest> getRequests() {
        return requests;
    }

    public int getRequestsCount() {
        return requests.size();
    }

    /**
     * Splits the data into different requests.
     *
     * @param data The data to split.
     * @return The list of requests.
     */
    private List<DataRequest> splitRequests(byte[] data) {
        List<DataRequest> requests = new ArrayList<>();

        int currentIndex = 0;
        while (currentIndex != data.length) {
            int headerSize = calculateHeaderSize(data);
            byte[] headerBytes = new byte[headerSize];
            System.arraycopy(data, currentIndex, headerBytes, 0, headerSize);

            Header header = new Header(headerBytes);
            byte[] body = new byte[data.length - headerSize];
            System.arraycopy(data, currentIndex + headerSize, body, 0, body.length);

            DataRequest request = new DataRequest(header, body);

            requests.add(request);
            currentIndex += headerSize + body.length;
        }

        return requests;
    }

    private int calculateHeaderSize(byte[] data) {
        byte[] headerSuffixBytes = ApiConstants.RequestFormatConstants.HEADER_SUFFIX.getBytes(StandardCharsets.UTF_8);
        int suffixIndex = BytesUtils.indexOf(data, headerSuffixBytes);

        if (suffixIndex == -1) {
            throw new IllegalArgumentException("Invalid data: Header not found");
        }

        return suffixIndex + headerSuffixBytes.length;
    }
}
