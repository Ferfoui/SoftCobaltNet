package fr.ferfoui.softcobalt.api.requestformat.header;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.BytesUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderFormatUtils {

    private static final String HEADER_PREFIX = ApiConstants.RequestFormatConstants.HEADER_PREFIX;
    private static final String HEADER_SUFFIX = ApiConstants.RequestFormatConstants.HEADER_SUFFIX;
    private static final int HEADER_MINIMAL_SIZE = HEADER_PREFIX.length() + HEADER_SUFFIX.length();

    private static final String KEYWORD_VALUE_SPLITTER = ApiConstants.RequestFormatConstants.KEYWORD_VALUE_SPLITTER;

    /**
     * Creates a header for a request.
     *
     * @param header The header of the message.
     * @return The message in the format "start_header:::header:::end_header" in a byte array.
     */
    public static byte[] createHeader(String header) {
        return (HEADER_PREFIX + header + HEADER_SUFFIX).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Extracts the header from a byte array.
     *
     * @param headerBytes The byte array containing the header.
     * @return The header as a string.
     */
    public static String extractHeader(byte[] headerBytes) {
        return BytesUtils.extractStringFromByteArray(headerBytes, HEADER_PREFIX, HEADER_SUFFIX);
    }

    /**
     * Checks if a byte array contains the correct header.
     *
     * @param data The byte array to check.
     * @return True if the byte array contains the correct header, false otherwise.
     */
    public static boolean doesDataContainCorrectHeader(byte[] data) {
        if (data == null || data.length < HEADER_MINIMAL_SIZE){
            return false;
        }

        return BytesUtils.doesByteArrayContain(data, HEADER_PREFIX.getBytes(StandardCharsets.UTF_8), HEADER_SUFFIX.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the indexes of the headers in the data.
     *
     * @param data The data to analyze.
     * @return The indexes of the headers.
     */
    public static List<Integer> extractHeaderIndexes(byte[] data) {
        return BytesUtils.indexesOf(data, HEADER_PREFIX.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Calculates the size of the header in the data.
     *
     * @param data The data to analyze.
     * @return The size of the header.
     */
    public static int calculateHeaderSize(byte[] data) {
        byte[] headerSuffixBytes = ApiConstants.RequestFormatConstants.HEADER_SUFFIX.getBytes(StandardCharsets.UTF_8);
        int suffixIndex = BytesUtils.indexOf(data, headerSuffixBytes);

        if (suffixIndex == -1) {
            throw new IllegalArgumentException("Invalid data: Header not found");
        }

        return suffixIndex + headerSuffixBytes.length;
    }

    /**
     * Extracts the secondary keywords from the header content.
     *
     * @param headerContentSplit The header content split by the keyword sub splitter.
     * @return The secondary keywords.
     */
    public static Map<String, String> extractSecondaryKeywords(String[] headerContentSplit) {
        return Arrays.stream(headerContentSplit).skip(1)
                .filter(keyword ->
                        keyword.contains(KEYWORD_VALUE_SPLITTER)
                )
                .map(keyword ->
                        keyword.split(KEYWORD_VALUE_SPLITTER)
                )
                .collect(HashMap::new, (map, entry) -> map.put(entry[0], entry[1]), HashMap::putAll);
    }
}
