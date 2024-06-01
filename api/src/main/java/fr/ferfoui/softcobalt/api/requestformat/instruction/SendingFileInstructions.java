package fr.ferfoui.softcobalt.api.requestformat.instruction;

import java.util.UUID;

public class SendingFileInstructions implements FileManagingInstructions {

    private final String directoryPath;
    private final UUID uuid;

    /**
     * Creates a new instance of the instructions.
     *
     * @param directoryPath The path of the directory where the files are stored.
     */
    public SendingFileInstructions(String directoryPath) {
        this(directoryPath, UUID.randomUUID());
    }

    public SendingFileInstructions(String directoryPath, UUID uuid) {
        this.directoryPath = directoryPath;
        this.uuid = uuid;
    }

    /**
     * Returns the path of the directory where the files are stored.
     *
     * @return The path of the directory where the files are stored.
     */
    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Returns the UUID of the instructions.
     *
     * @return The UUID of the instructions.
     */
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
