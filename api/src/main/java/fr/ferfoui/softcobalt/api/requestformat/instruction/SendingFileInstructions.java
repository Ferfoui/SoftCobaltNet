package fr.ferfoui.softcobalt.api.requestformat.instruction;

public class SendingFileInstructions implements FileManagingInstructions {

    private final String directoryPath;

    public SendingFileInstructions(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public String getDirectoryPath() {
        return directoryPath;
    }
}
