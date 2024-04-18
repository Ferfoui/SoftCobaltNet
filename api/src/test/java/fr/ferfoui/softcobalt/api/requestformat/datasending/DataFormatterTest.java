package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DataFormatterTest {

    @Test
    public void testCreateStringRequest() {
        DataFormatter dataFormatter = new DataFormatter();

        String SAMPLE_TEXT = "Hello World!";
        byte[] request = dataFormatter.createStringRequest(SAMPLE_TEXT);

        DataReader dataReader = new DataReader(request);

        Header header = dataReader.readHeader();

        String body = new String(dataReader.readBody());

        assertEquals(SAMPLE_TEXT, body);
        assertNotNull(header.getHeaderBytes());
    }

}
