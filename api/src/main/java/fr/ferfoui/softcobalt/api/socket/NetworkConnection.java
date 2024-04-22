package fr.ferfoui.softcobalt.api.socket;

import java.io.IOException;

public interface NetworkConnection<T> {

    /**
     * Read data from the connection
     *
     * @return The data read
     * @throws IOException If the data cannot be read
     */
    T readData() throws IOException;

    /**
     * Send data to the connection
     *
     * @param data The data to send
     * @throws IOException If the data cannot be sent
     */
    void sendData(T data) throws IOException;

    /**
     * Close the connection
     *
     * @throws IOException If the connection cannot be closed
     */
    void close() throws IOException;

}
