package fr.ferfoui.softcobalt.api.requestformat.instruction;

import java.io.Serializable;
import java.util.UUID;

public interface Instructions extends Serializable {

    /**
     * Returns the aim of the instructions.
     *
     * @return The instructions object.
     */
    InstructionObject getInstructionObject();

    /**
     * Returns the UUID of the instructions.
     *
     * @return The UUID of the instructions.
     */
    UUID getUUID();

}
