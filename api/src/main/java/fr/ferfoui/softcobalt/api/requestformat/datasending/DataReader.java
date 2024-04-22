package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.header.HeaderFormatUtils;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataReader implements RequestReader {

    private final List<DataRequest> requests;

    /**
     * Creates a new data reader with a byte array.
     *
     * @param data The byte array containing the data.
     */
    public DataReader(byte[] data) {
        this.requests = splitRequests(data);
    }

    /**
     * Return the requests contained in the data.
     *
     * @return The list of requests.
     */
    @Override
    public List<DataRequest> getRequests() {
        return requests;
    }

    /**
     * Return the number of requests contained in the data.
     *
     * @return The number of requests.
     */
    @Override
    public int getRequestsCount() {
        return requests.size();
    }

    /**
     * Splits the provided data into different requests.
     * This method takes a byte array as input, representing the data to be split into requests.
     * It iterates over the data, extracting the header and body for each request and creating a new DataRequest object.
     * Each DataRequest object is then added to a list, which is returned at the end of the method.
     *
     * @param data The byte array containing the data to be split into requests.
     * @return A list of DataRequest objects, each representing a single request extracted from the data.
     */
    private List<DataRequest> splitRequests(byte[] data) {
        List<DataRequest> requests = new ArrayList<>();

        if (data == null || data.length == 0 || !HeaderFormatUtils.doesDataContainCorrectHeader(data)) {
            return requests;
        }

        // Extract the indexes of the headers in the data
        List<Integer> headerIndexes = HeaderFormatUtils.extractHeaderIndexes(data);

        // Iterate over the header indexes
        for (int i = 0; i < headerIndexes.size(); i++) {
            DataRequest request = getRequest(data, headerIndexes, i);
            requests.add(request);
        }

        // Return the list of DataRequest objects
        return requests;
    }

    /**
     * Extracts a single request from the data based on the header indexes.
     * This method takes the data byte array, a list of header indexes, and the index of the current header to extract.
     * It calculates the start and end indexes for the current request based on the header indexes.
     * It then extracts the header and body from the data, creates a new DataRequest object, and returns it.
     *
     * @param data          The byte array containing the data.
     * @param headerIndexes The list of indexes where the headers are located in the data.
     * @param i             The index of the current header to extract.
     * @return A DataRequest object representing the current request extracted from the data.
     */
    private static @NotNull DataRequest getRequest(byte[] data, List<Integer> headerIndexes, int i) {
        int startIndex = headerIndexes.get(i);
        int endIndex = i + 1 < headerIndexes.size() ? headerIndexes.get(i + 1) : data.length;

        // Extract the header and body from the data
        byte[] requestBytes = Arrays.copyOfRange(data, startIndex, endIndex);

        int headerSize = HeaderFormatUtils.calculateHeaderSize(requestBytes);

        byte[] headerBytes = Arrays.copyOfRange(requestBytes, 0, headerSize);
        byte[] body = Arrays.copyOfRange(requestBytes, headerSize, requestBytes.length);

        // Create a new DataRequest object with the extracted header and body
        return new DataRequest(new Header(headerBytes), body);
    }

}
