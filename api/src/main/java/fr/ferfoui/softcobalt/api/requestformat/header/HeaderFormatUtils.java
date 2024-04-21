package fr.ferfoui.softcobalt.api.requestformat.header;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.datasending.BytesUtils;

import java.nio.charset.StandardCharsets;

public class HeaderFormatUtils {

    private static final String HEADER_PREFIX = ApiConstants.RequestFormatConstants.HEADER_PREFIX;
    private static final String HEADER_SUFFIX = ApiConstants.RequestFormatConstants.HEADER_SUFFIX;
    private static final int HEADER_MINIMAL_SIZE = HEADER_PREFIX.length() + HEADER_SUFFIX.length();

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
     * @param data The byte array containing the header.
     * @return The header as a string.
     */
    public static String extractHeader(byte[] data) {
        return extractStringFromByteArray(data, HEADER_PREFIX, HEADER_SUFFIX);
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

        return BytesUtils.doesByteArrayContain(data, HEADER_PREFIX.getBytes(StandardCharsets.UTF_8))
                && BytesUtils.doesByteArrayContain(data, HEADER_SUFFIX.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * This method extracts a string from a byte array, given a prefix and suffix.
     * The method first converts the byte array into a string using UTF-8 encoding.
     * It then finds the indices of the prefix and suffix in the string.
     * If either the prefix or suffix is not found, the method returns null.
     * Otherwise, it returns the substring between the prefix and suffix.
     *
     * @param data   The byte array containing the string. This should be non-null.
     * @param prefix The prefix to delete from the string. This should be non-null.
     * @param suffix The suffix to delete from the string. This should be non-null.
     * @return The string extracted from the byte array, or null if the prefix or suffix is not found.
     */
    public static String extractStringFromByteArray(byte[] data, String prefix, String suffix) {
        String dataString = new String(data, StandardCharsets.UTF_8);

        int prefixIndex = dataString.indexOf(prefix);
        int suffixIndex = dataString.indexOf(suffix);

        if (prefixIndex == -1 || suffixIndex == -1) {
            return null;
        }

        return dataString.substring(prefixIndex + prefix.length(), suffixIndex);
    }

}
