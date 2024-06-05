package fr.ferfoui.softcobalt.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DirectoryManager {

    public static void saveFile(Path filePath, byte[] data) {
        if (filePath == null) {
            throw new IllegalArgumentException("The file path cannot be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("The data cannot be null");
        }

        createDirectoryIfNotExists(filePath.getParent().toString());

        try {
            Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + filePath, e);
        }
    }

    public static void createDirectoryIfNotExists(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("The directory path cannot be null");
        }
        if (directoryPath.isEmpty()) {
            throw new IllegalArgumentException("The directory path cannot be empty");
        }
        if (!directoryPath.endsWith("/")) {
            directoryPath += "/";
        }

        Path path = Path.of(directoryPath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create directory: " + directoryPath, e);
            }
        }
    }

}
