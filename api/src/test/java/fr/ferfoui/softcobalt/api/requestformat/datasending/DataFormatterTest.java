package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.requestformat.PublicKeySendingUtils;
import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.requestformat.request.DataRequest;
import fr.ferfoui.softcobalt.api.security.key.AsymmetricKeysManager;
import fr.ferfoui.softcobalt.api.security.key.RsaKeysManager;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

    @Test
    public void testCreatePublicKeyRequest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        DataFormatter dataFormatter = new DataFormatter();

        AsymmetricKeysManager keysManager = new RsaKeysManager();
        keysManager.generateKeys();

        Key publicKey = keysManager.getPublicKey();
        String ALGORITHM = ApiConstants.SecurityConstants.RSA_ALGORITHM;

        byte[] request = dataFormatter.createPublicKeyRequest(publicKey, ALGORITHM);


        DataReader dataReader = new DataReader(request);
        int requestsCount = dataReader.getRequestsCount();
        assertEquals(1, requestsCount);

        DataRequest dataRequest = dataReader.getRequests().get(0);
        Header header = dataRequest.header();

        String foundAlgorithm = null;
        for (String keyword : header.getSecondaryKeywords()) {
            if (keyword.startsWith(ApiConstants.RequestFormatConstants.KEY_ALGORITHM_KEYWORD)) {
                foundAlgorithm = keyword.substring(ApiConstants.RequestFormatConstants.KEY_ALGORITHM_KEYWORD.length() + 1);
            }
        }

        assertNotNull(foundAlgorithm);

        byte[] body = dataRequest.body();
        Key decodedPublicKey = PublicKeySendingUtils.decodeReceivedPublicKey(body, foundAlgorithm);

        assertEquals(publicKey, decodedPublicKey);
        assertNotNull(header.getHeaderBytes());
    }

}
