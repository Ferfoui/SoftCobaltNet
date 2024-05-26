package fr.ferfoui.softcobalt.api.requestformat.instruction;

import java.io.Serializable;

public interface Instructions extends Serializable {

    /**
     * Returns the aim of the instructions.
     *
     * @return The instructions object.
     */
    InstructionObject getInstructionObject();

}
