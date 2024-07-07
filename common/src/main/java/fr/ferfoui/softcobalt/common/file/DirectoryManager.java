package fr.ferfoui.softcobalt.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This class provides utility methods for managing directories and files.
 */
public class DirectoryManager {

    /**
     * Saves the given data to a file at the specified path.
     * If the file does not exist, it will be created.
     * If the file exists, its contents will be overwritten.
     *
     * @param filePath the path of the file to save the data to
     * @param data     the data to save to the file
     * @throws IOException              if an I/O error occurs while writing to the file
     * @throws IllegalArgumentException if filePath or data is null
     */
    public static void saveFile(Path filePath, byte[] data) throws IOException {
        if (filePath == null)
            throw new IllegalArgumentException("The file path cannot be null");

        if (data == null)
            throw new IllegalArgumentException("The data cannot be null");

        createDirectoryIfNotExists(filePath.getParent().toString());

        Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param directoryPath the path of the directory to create
     * @throws IOException              if an I/O error occurs while creating the directory
     * @throws IllegalArgumentException if directoryPath is null or empty
     */
    public static void createDirectoryIfNotExists(String directoryPath) throws IOException {
        if (directoryPath == null)
            throw new IllegalArgumentException("The directory path cannot be null");

        if (directoryPath.isEmpty())
            throw new IllegalArgumentException("The directory path cannot be empty");

        if (!directoryPath.endsWith("/"))
            directoryPath += "/";

        Path path = Path.of(directoryPath);

        if (!Files.exists(path))
            Files.createDirectories(path);
    }

}
