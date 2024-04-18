package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;

public interface RequestReader {

    /**
     * Read the header of the request
     *
     * @return the header of the request
     */
    Header readHeader();

    /**
     * Read the body of the request
     *
     * @return the body of the request
     */
    byte[] readBody();

}
