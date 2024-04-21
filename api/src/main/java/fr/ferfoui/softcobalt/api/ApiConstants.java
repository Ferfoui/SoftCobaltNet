package fr.ferfoui.softcobalt.api;

/**
 * Constants used in the API
 *
 * @author Ferfoui
 * @since 1.0
 */
public class ApiConstants {

    /**
     * Constants related to security
     */
    public static class SecurityConstants {
        public static final String RSA_ALGORITHM = "RSA";
    }

    /**
     * Constants related to the request format
     */
    public static class RequestFormatConstants {
        public static final String REQUEST_SUB_SPLITTER = ":::";
        public static final String KEYWORD_SPLITTER = "|";
        public static final String KEYWORD_SUB_SPLITTER = "-";

        public static final String HEADER_PREFIX = "start_header" + REQUEST_SUB_SPLITTER;
        public static final String HEADER_SUFFIX = REQUEST_SUB_SPLITTER + "end_header";
        public static final int HEADER_PREFIX_SUFFIX_SIZE = HEADER_PREFIX.length() + HEADER_SUFFIX.length();

        public static final String PUBLIC_KEY_PREFIX = "public_key" + REQUEST_SUB_SPLITTER;
        public static final String PUBLIC_KEY_SUFFIX = REQUEST_SUB_SPLITTER + "end_public_key";

        public static final String FILENAME_KEYWORD = "filename";
        public static final String KEY_ALGORITHM_KEYWORD = "key_algorithm";
    }
}
