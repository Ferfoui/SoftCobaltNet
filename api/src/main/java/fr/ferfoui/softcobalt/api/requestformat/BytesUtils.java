package fr.ferfoui.softcobalt.api.requestformat;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains utility methods for working with byte arrays.
 */
public class BytesUtils {

    /**
     * This method is used to concatenate multiple byte arrays into a single byte array.
     *
     * @param arrays The byte arrays to concatenate.
     * @return The concatenated byte array.
     */
    public static byte[] concatenateByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }

        return result;
    }

    /**
     * This method is used to check if a byte array contains a specific pattern.
     *
     * @param outerArray The byte array in which we are trying to find the pattern.
     * @param pattern    The pattern we are trying to find in the byte array.
     * @return The method returns true if the pattern is found in the byte array, false otherwise.
     */
    public static boolean doesByteArrayContain(byte[] outerArray, byte[] pattern) {
        return indexOf(outerArray, pattern) != -1;
    }

    /**
     * This method is used to check if a byte array contains two specific patterns.
     * It is more efficient than calling the method twice for each pattern.
     *
     * @param outerArray The byte array in which we are trying to find the pattern.
     * @param pattern1   The first pattern we are trying to find in the byte array.
     * @param pattern2   The second pattern we are trying to find in the byte array.
     * @return The method returns true if both patterns are found in the byte array, false otherwise.
     */
    public static boolean doesByteArrayContain(byte[] outerArray, byte[] pattern1, byte[] pattern2) {
        if (pattern1.length == 0 || pattern2.length == 0) return false;

        byte[] biggerPattern = pattern1.length < pattern2.length ? pattern2 : pattern1;
        byte[] smallerPattern = pattern1.length < pattern2.length ? pattern1 : pattern2;

        boolean found1 = false;
        boolean found2 = false;

        // Iterate over the outer array up to the point where the bigger pattern could still fully fit
        int readLimit = outerArray.length - biggerPattern.length + 1;

        for (int i = 0; i < readLimit; ++i) {
            boolean tempFound1 = true;
            boolean tempFound2 = true;

            // Iterate over the bigger array and check if all its elements are found in the outer array at the current position
            for (int j = 0; j < biggerPattern.length; ++j) {
                // If the current element of the smaller array does not match the corresponding element in the outer array, break the loop

                if (!found1 && tempFound1 && (outerArray[i + j] != biggerPattern[j])) {
                    tempFound1 = false;
                    if (found2 || !tempFound2) break;
                }

                if (!found2 && tempFound2 && (j < smallerPattern.length) && (outerArray[i + j] != smallerPattern[j])) {
                    tempFound2 = false;
                    if (found1 || !tempFound1) break;
                }
            }

            found1 = found1 || tempFound1;
            found2 = found2 || tempFound2;

            if (found1 && found2) return true;
            else if (found1)
                readLimit = outerArray.length - smallerPattern.length + 1;
        }

        // If the arrays were not found in the outer array, return false
        return false;
    }

    /**
     * This method is used to find the first occurrence of a smaller byte array within a larger byte array.
     *
     * @param outerArray   The larger byte array in which we are trying to find the smaller byte array.
     * @param smallerArray The smaller byte array which we are trying to find within the larger byte array.
     * @return The method returns the first occurrence index of the smaller byte array within the larger byte array.
     * If the smaller byte array is not found within the larger byte array, the method returns -1.
     */
    public static int indexOf(byte[] outerArray, byte[] smallerArray) {
        if (smallerArray.length == 0) return -1;
        // Iterate over the outer array up to the point where the smaller array could still fully fit
        for (int i = 0; i < (outerArray.length - smallerArray.length + 1); ++i) {
            boolean found = true;

            // Iterate over the smaller array
            // and check if all its elements are found in the outer array at the current position
            for (int j = 0; j < smallerArray.length; ++j) {
                // If the current element of the smaller array does not match the corresponding element in the outer array,
                // break the loop
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }

            // If all elements of the smaller array were found in the outer array at the current position,
            // return the current position
            if (found) return i;
        }

        // If the smaller array was not found in the outer array, return -1
        return -1;
    }

    /**
     * This method is used to find all occurrences of a smaller byte array within a larger byte array.
     *
     * @param outerArray   The larger byte array in which we are trying to find the smaller byte array.
     * @param smallerArray The smaller byte array which we are trying to find within the larger byte array.
     * @return The method returns a list of indexes where the smaller byte array is found within the larger byte array.
     * If the smaller byte array is not found within the larger byte array, the method returns an empty list.
     */
    public static List<Integer> indexesOf(byte[] outerArray, byte[] smallerArray) {

        if (smallerArray.length == 0) return new ArrayList<>(0);

        List<Integer> indexes = new ArrayList<>();
        // Iterate over the outer array up to the point where the smaller array could still fully fit
        for (int i = 0; i < (outerArray.length - smallerArray.length + 1); ++i) {
            boolean found = true;

            // Iterate over the smaller array
            // and check if all its elements are found in the outer array at the current position
            for (int j = 0; j < smallerArray.length; ++j) {
                // If the current element of the smaller array does not match the corresponding element in the outer array,
                // break the loop
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }

            // If all elements of the smaller array were found in the outer array at the current position,
            // return the current position
            if (found) indexes.add(i);
        }

        return indexes;
    }

    /**
     * This method extracts a string from a byte array, given a prefix and suffix.
     * If either the prefix or suffix is not found, the method returns null.
     * Otherwise, it returns the substring between the prefix and suffix.
     *
     * @param data   The byte array containing the string. This should be non-null.
     * @param prefix The prefix to delete from the string. This should be non-null.
     * @param suffix The suffix to delete from the string. This should be non-null.
     * @return The string extracted from the byte array, or null if the prefix or suffix is not found.
     */
    @Nullable
    public static String extractStringFromByteArray(byte[] data, String prefix, String suffix) {
        String dataString = new String(data, StandardCharsets.UTF_8);

        int prefixIndex = dataString.indexOf(prefix);
        int suffixIndex = dataString.indexOf(suffix);

        if (prefixIndex == -1 || suffixIndex == -1) {
            return null;
        }

        return dataString.substring(prefixIndex + prefix.length(), suffixIndex);
    }

}
