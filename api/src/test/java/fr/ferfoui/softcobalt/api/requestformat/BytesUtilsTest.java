package fr.ferfoui.softcobalt.api.requestformat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BytesUtilsTest {

    public static byte[] createRandomByteArray(int size) {
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = (byte) (Math.random() * 256);
        }
        return array;
    }

    private List<byte[]> createRandomMatchingPatterns(int listSize, byte[] originalArray) {

        List<byte[]> matchingPatterns = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {

            int patternSize = (int) (Math.random() * originalArray.length-1) + 1; // Pattern size should be between 1 and data.length
            int startIndex = (int) (Math.random() * (originalArray.length - patternSize));

            byte[] pattern = new byte[patternSize];
            System.arraycopy(originalArray, startIndex, pattern, 0, patternSize);

            matchingPatterns.add(pattern);
        }
        return matchingPatterns;
    }

    private List<byte[]> createRandomNonMatchingPatterns(int listSize, byte[] originalArray) {

        List<byte[]> nonMatchingPatterns = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {

            int patternSize = (int) (Math.random() * originalArray.length*2); // Pattern size should be between 0 and data.length*2

            byte[] pattern = createRandomByteArray(patternSize);

            if (!BytesUtils.doesByteArrayContain(originalArray, pattern))
                nonMatchingPatterns.add(pattern);
            else i--;
        }
        return nonMatchingPatterns;
    }

    @Test
    public void testConcatenateByteArrays() {
        byte[] array1 = createRandomByteArray(10);
        byte[] array2 = createRandomByteArray(20);
        byte[] array3 = createRandomByteArray(30);

        byte[] concatenatedArray = BytesUtils.concatenateByteArrays(array1, array2, array3);

        assertEquals(array1.length + array2.length + array3.length, concatenatedArray.length,
                "The concatenated array should have the sum of the lengths of the input arrays");

        for (int i = 0; i < array1.length; i++) {
            assertEquals(array1[i], concatenatedArray[i],
                    "The concatenated array should contain the first array");
        }

        for (int i = 0; i < array2.length; i++) {
            assertEquals(array2[i], concatenatedArray[i + array1.length],
                    "The concatenated array should contain the second array");
        }

        for (int i = 0; i < array3.length; i++) {
            assertEquals(array3[i], concatenatedArray[i + array1.length + array2.length],
                    "The concatenated array should contain the third array");
        }
    }

    @Test
    public void testDoesByteArrayContainSimple() {

        byte[] data = createRandomByteArray(50);

        List<byte[]> matchingPatterns = createRandomMatchingPatterns(100, data);
        List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(100, data);

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

        byte[] data = createRandomByteArray(50);

        List<byte[]> matchingPatterns = createRandomMatchingPatterns(200, data);
        List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(200, data);

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

    @Test
    public void testDoesByteArrayContainTwoPatternsWithAHugeDataLength() {
        byte[] data = createRandomByteArray(5000);

        List<byte[]> matchingPatterns = createRandomMatchingPatterns(50, data);
        List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(50, data);

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

    @Test
    public void testIndexOf() {
        byte[] data = createRandomByteArray(30);

        List<byte[]> matchingPatterns = createRandomMatchingPatterns(80, data);
        List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(80, data);

        for (byte[] pattern : matchingPatterns) {
            int index = BytesUtils.indexOf(data, pattern);
            assertTrue(index != -1, "Pattern " + Arrays.toString(pattern) + " should be found in data");

            assertArrayEquals(
                    pattern, Arrays.copyOfRange(data, index, index + pattern.length),
                    "Pattern " + Arrays.toString(pattern) + " should be found at index " + index
            );
        }

        for (byte[] pattern : nonMatchingPatterns) {
            int index = BytesUtils.indexOf(data, pattern);
            assertEquals(-1, index, "Pattern " + Arrays.toString(pattern) + " should not be found in data");
        }
    }

    @Test
    public void testIndexesOf() {
        byte[] data = createRandomByteArray(30);

        List<byte[]> matchingPatterns = createRandomMatchingPatterns(80, data);
        List<byte[]> nonMatchingPatterns = createRandomNonMatchingPatterns(80, data);

        for (byte[] pattern : matchingPatterns) {
            List<Integer> indexes = BytesUtils.indexesOf(data, pattern);
            assertFalse(indexes.isEmpty(), "Pattern " + Arrays.toString(pattern) + " should be found in data");

            for (int index : indexes) {
                assertArrayEquals(
                        pattern, Arrays.copyOfRange(data, index, index + pattern.length),
                        "Pattern " + Arrays.toString(pattern) + " should be found at index " + index
                );
            }
        }

        for (byte[] pattern : nonMatchingPatterns) {
            List<Integer> indexes = BytesUtils.indexesOf(data, pattern);
            assertEquals(0, indexes.size(), "Pattern " + Arrays.toString(pattern) + " should not be found in data");
        }
    }

    @Test
    public void testExtractStringFromByteArray() {
        byte[] data = "start_header:::header:::end_header".getBytes();

        String extractedString = BytesUtils.extractStringFromByteArray(data, "start_header:::", ":::end_header");
        assertEquals("header", extractedString, "The extracted string should be 'header'");
    }

}
