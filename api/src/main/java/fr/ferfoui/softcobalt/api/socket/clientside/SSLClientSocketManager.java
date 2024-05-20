package fr.ferfoui.softcobalt.api.socket.clientside;

import fr.ferfoui.softcobalt.api.ApiConstants;
import fr.ferfoui.softcobalt.api.socket.DataQueueSocketManager;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to manage the client socket connection.
 *
 * @author Ferfoui
 * @since 1.0
 */
public class SSLClientSocketManager
    extends DataQueueSocketManager implements ClientNetworkConnection<byte[]> {

  private final Thread queueHandlerThread =
      new Thread(getQueueHandlerRunnable());
  private DataInputStream in;
  private DataOutputStream out;

  public SSLClientSocketManager(@Nullable Logger logger) {
    super(null, logger);
  }

  /**
   * This method is returning the logger to use.
   *
   * @param logger The logger to use, if null, a new logger will be created
   * @return The logger to use
   */
  @Override
  protected Logger getLogger(@Nullable Logger logger) {
    return (logger == null) ? LoggerFactory.getLogger("client-socket-manager")
                            : logger;
  }

  /**
   * Start the connection to the server
   *
   * @param ip   The IP of the server
   * @param port The port of the server
   * @throws IOException If the connection cannot be established
   */
  @Override
  public void startConnection(String ip, int port) throws IOException {
    logger.info("Starting connection to {}:{}", ip, port);
    socket = createSSLSocket(ip, port);
    queueHandlerThread.start();
    in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    out = new DataOutputStream(socket.getOutputStream());
  }

  /**
   * Create an SSL socket to the server
   *
   * @param ip   The IP of the server
   * @param port The port used to connect to the server
   * @return The created SSL socket
   * @throws IOException If the socket cannot be created
   */
  private SSLSocket createSSLSocket(String ip, int port) throws IOException {
    SSLSocket sslSocket =
        (SSLSocket)SSLSocketFactory.getDefault().createSocket(ip, port);

    sslSocket.setEnabledProtocols(
        ApiConstants.SecurityConstants.SECURITY_PROTOCOLS);
    sslSocket.setEnabledCipherSuites(
        ApiConstants.SecurityConstants.CIPHER_SUITES);

    try {
      sslSocket.startHandshake();
    } catch (IOException e) {
      logger.error("Error while starting handshake", e);
      sslSocket.close();
      throw new RuntimeException("Error while starting handshake", e);
    }

    return sslSocket;
  }

  /**
   * Send bytes to the server
   *
   * @param bytesToSend The bytes to send
   * @throws IOException If the bytes cannot be sent
   */
  @Override
  public void sendData(byte[] bytesToSend) throws IOException {
    logger.info("Sending bytes: {}",
                new String(bytesToSend, StandardCharsets.UTF_8));
    out.write(bytesToSend);
  }

  /**
   * Receive bytes from the server
   *
   * @return The received bytes
   * @throws IOException If the bytes cannot be received
   */
  @Override
  public byte[] readData() throws IOException {
    byte[] bytesReceived = new byte[in.available()];
    in.readFully(bytesReceived);
    logger.info("Received bytes: {}",
                new String(bytesReceived, StandardCharsets.UTF_8));
    return bytesReceived;
  }

  /**
   * Stop the connection to the server
   *
   * @throws IOException If the connection cannot be closed
   */
  @Override
  public void closeConnection() throws IOException {
    logger.info("Stopping connection");
    queueHandlerThread.interrupt();
    in.close();
    out.close();
    socket.close();
  }

  /**
   * Check if the connection is active
   *
   * @return True if the connection is active, false otherwise
   */
  @Override
  public boolean isConnected() {
    if (socket == null) {
      return false;
    }
    return socket.isConnected();
  }
}
