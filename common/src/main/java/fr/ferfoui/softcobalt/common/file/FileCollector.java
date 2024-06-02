package fr.ferfoui.softcobalt.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for collecting all file paths from a given directory.
 * It supports both relative and absolute paths and can handle nested directories.
 */
public class FileCollector {

    // The path of the directory to collect files from
    private final Path directoryPath;
    // A list to store the collected file paths
    private final List<Path> filePaths = new ArrayList<>();

    /**
     * Constructor that accepts a directory path as a string.
     *
     * @param directoryPath the path of the directory to collect files from
     * @throws IllegalArgumentException if the provided path does not exist or is not a directory
     */
    public FileCollector(String directoryPath) {
        this(Path.of(directoryPath));
    }

    /**
     * Constructor that accepts a directory path as a Path object.
     * It checks if the provided path exists and if it is a directory.
     *
     * @param directoryPath the path of the directory to collect files from
     * @throws IllegalArgumentException if the provided path does not exist or is not a directory
     */
    public FileCollector(Path directoryPath) {
        this.directoryPath = directoryPath;

        File absoluteDirectory = this.directoryPath.toAbsolutePath().toFile();
        if (!absoluteDirectory.exists()) {
            throw new IllegalArgumentException("The provided path does not exist: " + directoryPath);
        }
        if (absoluteDirectory.isFile()) {
            throw new IllegalArgumentException("The provided path is not a directory: " + directoryPath);
        }
    }

    /**
     * Collects all file paths from the directory.
     * It uses a private helper method to handle nested directories.
     *
     * @throws IOException if an I/O error occurs
     */
    public void collectFiles() throws IOException {
        try {
            collectFilesRecursively(directoryPath, "");

        } catch (IOException e) {
            throw new IOException("Could not collect files from directory: " + directoryPath.toAbsolutePath(), e);
        }
    }

    /**
     * Recursively collects all file paths from a directory.
     * It uses a DirectoryStream to iterate over the directory's files.
     *
     * @param directoryPath    the path of the directory to collect files from
     * @param subdirectoryName the name of the current subdirectory
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Returns the path of the directory.
     *
     * @return the path of the directory
     */
    public Path getDirectory() {
        return directoryPath;
    }

    /**
     * Returns the list of collected file paths.
     *
     * @return the list of collected file paths
     */
    public List<Path> getFilePaths() {
        return filePaths;
    }
}
