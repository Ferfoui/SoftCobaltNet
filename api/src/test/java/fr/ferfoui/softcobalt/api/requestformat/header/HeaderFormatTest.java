package fr.ferfoui.softcobalt.api.requestformat.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderFormatTest {

    @Test
    public void testHeaderCreation() {
        Header header = new Header(HeaderPrincipalKeyword.FILE);
        header.addSecondaryKeywords("filename=myFile.zip");

        assertEquals(HeaderPrincipalKeyword.FILE.getKeyword(), header.getPrincipalKeyword());
        assertEquals("filename=myFile.zip", header.getSecondaryKeywords().get(0));
    }

    @Test
    public void testCalculateHeaderSize() {
        byte[] data = "start_header:::header:::end_header".getBytes();
        assertEquals(34, HeaderFormatUtils.calculateHeaderSize(data));
    }
}
