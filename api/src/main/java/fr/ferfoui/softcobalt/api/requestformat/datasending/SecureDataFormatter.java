package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.header.Header;
import fr.ferfoui.softcobalt.api.security.EncryptDecryptCipher;
import fr.ferfoui.softcobalt.api.security.SecureExchanger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Formatter used to send data securely.
 * The data can be encrypted using a key.
 * The encryption is disabled by default.
 * The key must be set before enabling the encryption.
 * The encryption is done using the key and the algorithm associated with it.
 * The encryption is done using the {@link EncryptDecryptCipher#encrypt(byte[],
 * Key, String)} method.
 */
@Deprecated
public class SecureDataFormatter
    extends DataFormatter implements SecureExchanger {

  private Key key;
  private String keyAlgorithm;
  private boolean isEncryptionEnabled = false;

  /**
   * Create a request with a header and a body
   *
   * @param header the header of the request
   * @param body   the body of the request
   * @return the request
   */
  @Override
  public byte[] createRequest(Header header, byte[] body) {

    byte[] clearRequest = super.createRequest(header, body);

    if (!isEncryptionEnabled)
      return clearRequest;

    return encryptRequest(clearRequest);
  }

  /**
   * Encrypt the request.
   *
   * @param clearRequest the clear request
   * @return the encrypted request
   */
  private byte[] encryptRequest(byte[] clearRequest) {
    try {
      return EncryptDecryptCipher.encrypt(clearRequest, key, keyAlgorithm);
    } catch (NoSuchPaddingException | NoSuchAlgorithmException |
             InvalidKeyException | IllegalBlockSizeException |
             BadPaddingException e) {
      throw new RuntimeException("Error while encrypting the request",
                                 e.getCause());
    }
  }

  /**
   * Retrieves the security key.
   *
   * @return The security key.
   */
  @Override
  public Key getKey() {
    return key;
  }

  /**
   * Sets the security key and its associated algorithm.
   *
   * @param key       The security key.
   * @param algorithm The algorithm associated with the key.
   */
  @Override
  public void setKey(Key key, String algorithm) {
    this.key = key;
    this.keyAlgorithm = algorithm;
  }

  /**
   * Retrieves the algorithm associated with the security key.
   *
   * @return The algorithm of the key.
   */
  @Override
  public String getKeyAlgorithm() {
    return keyAlgorithm;
  }

  /**
   * Enables or disables the encryption of the data.
   * The encryption is disabled by default, and the key must be set before
   * enabling it.
   *
   * @param isEnable True to enable the encryption, false to disable it.
   */
  public void enableEncryption(boolean isEnable) {
    if (isEnable && key == null) {
      throw new IllegalStateException(
          "The key must be set before enabling the decryption.");
    }
    this.isEncryptionEnabled = isEnable;
  }
}
