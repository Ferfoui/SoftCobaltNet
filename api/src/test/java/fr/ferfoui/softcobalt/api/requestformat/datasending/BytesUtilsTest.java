package fr.ferfoui.softcobalt.api.requestformat.datasending;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BytesUtilsTest {

    byte[] data = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    byte[] matchingPattern = new byte[] {3, 4, 5};
    byte[] matchingPattern2 = new byte[] {6, 7, 8, 9};
    byte[] matchingPattern3 = new byte[] {8, 9, 10};
    byte[] matchingPattern4 = new byte[] {1, 2, 3};
    byte[] matchingPattern5 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    byte[] matchingPattern6 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    byte[] matchingPattern7 = new byte[] {8, 9, 10, 11, 12};
    byte[] matchingPattern8 = new byte[] {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};


    List<byte[]> matchingPatterns = List.of(
            matchingPattern, matchingPattern2, matchingPattern3, matchingPattern4, matchingPattern5, matchingPattern6, matchingPattern7, matchingPattern8
    );

    List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(100);

    private List<byte[]> createRandomMatchingPatterns(int listSize) {

        List<byte[]> matchingPatterns = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            byte[] pattern = new byte[(int) (Math.random() * 10)];
            for (int j = 0; j < pattern.length; j++) {
                pattern[j] = (byte) (Math.random() * 256);
            }
            matchingPatterns.add(pattern);
        }
        return matchingPatterns;
    }

    private List<byte[]> createRandomNonMatchingPatterns(int listSize) {

        List<byte[]> nonMatchingPatterns = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {

            byte[] pattern = new byte[(int) (Math.random() * data.length * 2)];
            for (int j = 0; j < pattern.length; j++) {
                pattern[j] = (byte) (Math.random() * 256);
            }
            if (!BytesUtils.doesByteArrayContain(data, pattern))
                nonMatchingPatterns.add(pattern);
            else i--;
        }
        return nonMatchingPatterns;
    }

    @Test
    public void testDoesByteArrayContainSimple() {

        for (byte[] pattern : matchingPatterns) {
            assertTrue(BytesUtils.doesByteArrayContain(data, pattern),
                    "Pattern " + Arrays.toString(pattern) + " should be found in data");
        }

        for (byte[] pattern : nonMatchingPatterns) {
            assertFalse(BytesUtils.doesByteArrayContain(data, pattern),
                    "Pattern " + Arrays.toString(pattern) + " should not be found in data");
        }
    }

    @Test
    public void testDoesByteArrayContainTwoPatterns() {
        for (byte[] pattern1 : matchingPatterns) {
            for (byte[] pattern2 : matchingPatterns) {
                assertTrue(BytesUtils.doesByteArrayContain(data, pattern1, pattern2),
                        "Patterns " + Arrays.toString(pattern1) + " and " + Arrays.toString(pattern2) + " should be found in data");
            }
        }

        for (byte[] pattern1 : nonMatchingPatterns) {
            for (byte[] pattern2 : nonMatchingPatterns) {
                assertFalse(BytesUtils.doesByteArrayContain(data, pattern1, pattern2),
                        "Patterns " + Arrays.toString(pattern1) + " and " + Arrays.toString(pattern2) + " should not be found in data");
            }
        }

        for (byte[] pattern1 : matchingPatterns) {
            for (byte[] pattern2 : nonMatchingPatterns) {
                assertFalse(BytesUtils.doesByteArrayContain(data, pattern1, pattern2),
                        "Patterns " + Arrays.toString(pattern1) + " and " + Arrays.toString(pattern2) + " should not be found in data");
                assertFalse(BytesUtils.doesByteArrayContain(data, pattern2, pattern1),
                        "Patterns " + Arrays.toString(pattern2) + " and " + Arrays.toString(pattern1) + " should not be found in data");
            }
        }
    }

}
