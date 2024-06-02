package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
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
        int requestsCount = dataReader.getRequestsCount();
        assertEquals(1, requestsCount);

        DataRequest dataRequest = dataReader.getRequests().get(0);
        Header header = dataRequest.header();
        String body = new String(dataRequest.body());

        assertEquals(SAMPLE_TEXT, body);
        assertNotNull(header.getHeaderBytes());
    }

}
