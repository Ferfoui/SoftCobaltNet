package fr.ferfoui.softcobalt.api.requestformat.header;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Header {

    private final String headerKeyword;
    private final Map<String, String> secondaryKeywords = new HashMap<>();

    private String fullHeaderContent;
    private int headerSize;

    /**
     * Creates a new header with a principal keyword and a size.
     *
     * @param keyword The principal keyword of the header.
     */
    public Header(HeaderPrincipalKeyword keyword) {
        this.headerKeyword = keyword.getKeyword();

        updateFullHeaderContent();
    }

    /**
     * Creates a new header from a byte array.
     *
     * @param headerBytes The byte array containing the header.
     */
    public Header(byte[] headerBytes) {
        String headerContent = HeaderFormatUtils.extractHeader(headerBytes);

        String[] headerContentSplit = headerContent.split(RequestFormatConstants.KEYWORD_SUB_SPLITTER);
        this.headerKeyword = headerContentSplit[0];
        this.headerSize = headerBytes.length;

        this.secondaryKeywords.putAll(HeaderFormatUtils.extractSecondaryKeywords(headerContentSplit));

        updateFullHeaderContent();
    }

    /**
     * Adds some secondary keywords to the header to make it more precise.
     *
     * @param keywords The secondary keywords to add (for example, if you want to send a file named "Jacopo.zip",
     *                 you can add "filename=Jacopo.zip").
     */
    public void addSecondaryKeywords(String... keywords) {
        Arrays.stream(keywords).forEach(keyword -> {
            String[] keywordSplit = keyword.split(RequestFormatConstants.KEYWORD_VALUE_SPLITTER);
            secondaryKeywords.put(keywordSplit[0], keywordSplit[1]);
        });

        updateFullHeaderContent();
    }

    /**
     * Adds some secondary keywords to the header to make it more precise.
     *
     * @param keywords The secondary keywords to add (for example, if you want to send a file named "myFile.zip",
     *                 you can add "filename" and "myFile.zip" in the {@link Map}).
     */
    public void addSecondaryKeywords(Map<String, String> keywords) {
        secondaryKeywords.putAll(keywords);

        updateFullHeaderContent();
    }

    /**
     * Updates the full header content and the header size.
     */
    private void updateFullHeaderContent() {
        StringBuilder fullHeaderContentBuilder = new StringBuilder(headerKeyword);
        secondaryKeywords.forEach((key, value) -> fullHeaderContentBuilder
                .append(RequestFormatConstants.KEYWORD_SUB_SPLITTER).append(key)
                .append(RequestFormatConstants.KEYWORD_VALUE_SPLITTER).append(value)
        );

        this.fullHeaderContent = fullHeaderContentBuilder.toString();
        this.headerSize = fullHeaderContent.getBytes().length + RequestFormatConstants.HEADER_PREFIX_SUFFIX_SIZE;
    }

    /**
     * Returns the header as a byte array.
     *
     * @return The header as a byte array.
     */
    public byte[] getHeaderBytes() {
        return HeaderFormatUtils.createHeader(fullHeaderContent);
    }

    /**
     * Returns the header as a {@link String}.
     *
     * @return The header as a {@link String}.
     */
    public String getHeaderString() {
        return fullHeaderContent;
    }

    /**
     * Returns the principal keyword of the header.
     *
     * @return The principal keyword of the header.
     */
    public String getPrincipalKeyword() {
        return headerKeyword;
    }

    /**
     * Returns the secondary keywords of the header as a {@link Map}.
     *
     * @return The secondary keywords of the header.
     */
    public Map<String, String> getSecondaryKeywords() {
        return secondaryKeywords;
    }

    /**
     * Returns the size of the header.
     *
     * @return The size of the header.
     */
    public int getHeaderSize() {
        return headerSize;
    }
}
