package fr.ferfoui.softcobalt.api.network;

import fr.ferfoui.softcobalt.api.requestformat.datasending.DataFormatter;
import fr.ferfoui.softcobalt.api.requestformat.datasending.DataReader;
import fr.ferfoui.softcobalt.api.requestformat.datasending.RequestFormatter;
import fr.ferfoui.softcobalt.api.socket.clientside.SSLClientSocketManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for managing the communication with the server.
 * It uses SSL for secure communication.
 */
public class CommunicationClient {

  private final SSLClientSocketManager connection;
  private final Logger logger;
  private final RequestFormatter requestFormatter;
  private final String ip;
  private final int port;

  private Thread queueHandlerThread;

  /**
   * Constructor for the CommunicationClient
   *
   * @param ip     The ip of the server to connect to
   * @param port   The port of the server to connect to
   * @param logger The logger to use, if null, a new logger will be created
   */
  public CommunicationClient(String ip, int port, @Nullable Logger logger) {
    this.ip = ip;
    this.port = port;
    this.connection = new SSLClientSocketManager(logger);

    this.logger = (logger == null)
                      ? LoggerFactory.getLogger("file-sender-client")
                      : logger;

    this.requestFormatter = new DataFormatter();
  }

  /**
   * Starts the communication with the server.
   * If the client is not already connected, it will attempt to connect.
   */
  public void startCommunication() {
    if (!connection.isConnected()) {
      try {
        connection.startConnection(ip, port);
      } catch (IOException e) {
        logger.error("Error while starting the communication", e);
      }

      queueHandlerThread = new Thread(connection.getQueueHandlerRunnable());
    }
  }

  /**
   * Closes the communication with the server.
   * If the client is connected, it will attempt to close the connection.
   */
  public void closeCommunication() {
    if (connection.isConnected()) {

      try {
        connection.closeConnection();
      } catch (IOException e) {
        logger.error("Error while closing the communication", e);
      }

      queueHandlerThread.interrupt();
    }
  }

  public void sendText(String text) {
    startCommunication();

    try {
      connection.sendData(requestFormatter.createStringRequest(text));
    } catch (IOException e) {
      logger.error("Error while sending the text", e);
    }
  }

  public void sendFile(File file) {
    startCommunication();

    try {
      connection.sendData(requestFormatter.createFileRequest(
          Files.readAllBytes(file.toPath()), file.getName()));
    } catch (IOException e) {
      logger.error("Error while sending the file", e);
    }
  }

  public byte[] readData() {
    startCommunication();

    byte[] data;
    try {
      data = connection.waitUntilDataAvailable();

      DataReader dataReader = new DataReader(data);

      return dataReader.getRequests().get(0).body();

    } catch (IOException e) {
      logger.error("Error while reading the data", e);
    }

    return null;
  }
}
