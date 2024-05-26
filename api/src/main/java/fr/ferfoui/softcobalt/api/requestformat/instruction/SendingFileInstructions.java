package fr.ferfoui.softcobalt.api.requestformat.instruction;

public class SendingFileInstructions implements FileManagingInstructions {

    private final String directoryPath;

    /**
     * Creates a new instance of the instructions.
     *
     * @param directoryPath The path of the directory where the files are stored.
     */
    public SendingFileInstructions(String directoryPath) {
        this.directoryPath = directoryPath;
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
}
