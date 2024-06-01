package fr.ferfoui.softcobalt.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileCollector {

    private final Path directoryPath;
    private final List<Path> filePaths = new ArrayList<>();

    public FileCollector(String directoryPath) {
        this.directoryPath = Path.of(directoryPath);

        File absoluteDirectory = this.directoryPath.toAbsolutePath().toFile();
        if (!absoluteDirectory.exists()) {
            throw new IllegalArgumentException("The provided path does not exist: " + directoryPath);
        }
        if (absoluteDirectory.isFile()) {
            throw new IllegalArgumentException("The provided path is not a directory: " + directoryPath);
        }
    }

    public void collectFiles() throws IOException {
        try {
            collectFilesRecursively(directoryPath, "");

        } catch (IOException e) {
            throw new IOException("Could not collect files from directory: " + directoryPath.toAbsolutePath(), e);
        }
    }

    private void collectFilesRecursively(Path directoryPath, String subdirectoryName) throws IOException {
        try (var fileStream = Files.newDirectoryStream(directoryPath)) {
            for (Path subPath : fileStream) {

                String recursivePath = (subdirectoryName.isEmpty()) ? subPath.toString()
                        : subdirectoryName + "/" + subPath.getFileName();

                if (subPath.toFile().isFile()) {
                    this.filePaths.add(Path.of(recursivePath));
                } else if (subPath.toFile().isDirectory()) {
                    collectFilesRecursively(subPath, recursivePath);
                }
            }
        }
    }

    public Path getDirectory() {
        return directoryPath;
    }

    public List<Path> getFilePaths() {
        return filePaths;
    }
}
