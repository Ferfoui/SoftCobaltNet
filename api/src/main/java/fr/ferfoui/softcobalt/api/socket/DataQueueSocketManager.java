package fr.ferfoui.softcobalt.api.socket;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An abstract class used to manage the socket connection to read data from the socket and put it in a queue.
 * The queue is used to wait for data to be available.
 */
public abstract class DataQueueSocketManager {

    protected final Logger logger;
    protected final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
    protected Socket socket;

    public DataQueueSocketManager(Socket socket, Logger logger) {
        this.socket = socket;
        this.logger = getLogger(logger);
    }

    /**
     * This method should be implemented by the subclasses to get the logger to use.
     *
     * @param logger The logger to use, if null, a new logger will be created
     * @return The logger to use
     */
    protected abstract Logger getLogger(@Nullable Logger logger);

    /**
     * Get the queue handler runnable, it will read the data from the socket and put it in the queue.
     * The queue is used to wait for data to be available.
     *
     * @return The queue handler thread
     */
    public Runnable getQueueHandlerRunnable() {
        return () -> {
            byte[] buffer = new byte[1024];
            int bytesRead;
            try {
                while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                    byte[] data = new byte[bytesRead];
                    System.arraycopy(buffer, 0, data, 0, bytesRead);
                    queue.put(data);
                }
            } catch (IOException | InterruptedException e) {
                if (!socket.isClosed()) {
                    logger.error("Error while reading data", e);
                } else {
                    logger.info("Socket closed, stopping queue handler");
                }
            }
        };
    }

    /**
     * Wait until data is available in the queue
     *
     * @return The available data in the queue
     */
    public byte[] waitUntilDataAvailable() throws IOException {
        try {
            return queue.take(); // This will block until data is available
        } catch (InterruptedException e) {
            logger.error("Error while waiting for data", e);
            return null;
        }
    }

    /**
     * Close the socket and the queue
     */
    public boolean isSocketClosed() {
        return socket.isClosed();
    }
}
