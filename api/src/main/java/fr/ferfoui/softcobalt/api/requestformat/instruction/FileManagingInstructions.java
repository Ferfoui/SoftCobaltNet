package fr.ferfoui.softcobalt.api.requestformat.instruction;

public interface FileManagingInstructions extends Instructions {

    @Override
    default InstructionObject getInstructionObject() {
        return InstructionObject.GOING_TO_SEND_FILES;
    }

    String getDirectoryPath();

}
