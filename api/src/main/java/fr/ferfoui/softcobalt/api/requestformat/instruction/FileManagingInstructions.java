package fr.ferfoui.softcobalt.api.requestformat.instruction;

public interface FileManagingInstructions extends Instructions {

    @Override
    default InstructionObject getInstructionObject() {
        return InstructionObject.GOING_TO_SEND_FILES;
    }

    /**
     * Returns the path of the directory where the files are stored.
     *
     * @return The path of the directory where the files are stored.
     */
    String getDirectoryPath();

}
