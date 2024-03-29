package fr.ferfoui.softcobalt.api.security.key;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

/**
 * Class used to save keys to files.
 *
 * @author Ferfoui
 * @see KeyFileReader
 * @since 1.0
 */
public class KeyFileSaver {

    /**
     * Save a key to a file
     *
     * @param key the key to save
     * @param file the file to save the key to
     * @throws IOException if an I/O error occurs
     */
    public static void saveKeyToFile(Key key, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key.getEncoded());
        fos.close();
    }
}
