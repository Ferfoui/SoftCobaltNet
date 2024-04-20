package fr.ferfoui.softcobalt.api.requestformat.header;

import fr.ferfoui.softcobalt.api.ApiConstants.RequestFormatConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Header {

    private final String headerKeyword;
    private final List<String> secondaryKeywords = new ArrayList<>();

    private String fullHeaderContent;
    private int headerSize;

    /**
     * Creates a new header with a principal keyword and a size.
     *
     * @param keyword    The principal keyword of the header.
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

        this.secondaryKeywords.addAll(
                Arrays.asList(headerContentSplit).subList(1, headerContentSplit.length)
        );

        updateFullHeaderContent();
    }

    /**
     * Adds some secondary keywords to the header to make it more precise.
     *
     * @param keywords The secondary keywords to add (for example, if you want to send a file named "myFile.zip",
     *                 you can add "filename=myFile.zip").
     */
    public void addSecondaryKeywords(String... keywords) {
        this.secondaryKeywords.addAll(List.of(keywords));
        updateFullHeaderContent();
    }

    private void updateFullHeaderContent() {
        StringBuilder fullHeaderContentBuilder = new StringBuilder(headerKeyword);
        for (String string : secondaryKeywords) {
            fullHeaderContentBuilder.append(RequestFormatConstants.KEYWORD_SUB_SPLITTER).append(string);
        }
        this.fullHeaderContent = fullHeaderContentBuilder.toString();
        this.headerSize = fullHeaderContent.getBytes().length + RequestFormatConstants.HEADER_PREFIX_SUFFIX_SIZE;
    }

    public byte[] getHeaderBytes() {
        return HeaderFormatUtils.createHeader(fullHeaderContent);
    }

    public String getHeaderString() {
        return fullHeaderContent;
    }

    public String getPrincipalKeyword() {
        return headerKeyword;
    }

    public List<String> getSecondaryKeywords() {
        return secondaryKeywords;
    }

    public int getHeaderSize() {
        return headerSize;
    }
}
