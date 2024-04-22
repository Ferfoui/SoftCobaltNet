package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.BytesUtils;
import fr.ferfoui.softcobalt.api.requestformat.request.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataReaderTest {

    @Test
    public void testRequestSplitting() {

        DataFormatter dataFormatter = new DataFormatter();
        byte[] requestBytes1 = dataFormatter.createStringRequest("Hello World!");
        byte[] requestBytes2 = dataFormatter.createStringRequest("Hello World again!");

        byte[] data = BytesUtils.concatenateByteArrays(requestBytes1, requestBytes2);

        DataReader dataReader = new DataReader(data);

        int requestsCount = dataReader.getRequestsCount();
        Request request1 = dataReader.getRequests().get(0);
        Request request2 = dataReader.getRequests().get(1);

        assertEquals(2, requestsCount, "The number of requests is incorrect.");
        assertArrayEquals(requestBytes1, request1.getBytes(), new String(request1.getBytes()) + " != \n" + new String(requestBytes1));
        assertArrayEquals(requestBytes2, request2.getBytes(), new String(request2.getBytes()) + " != \n" + new String(requestBytes2));
    }

}
