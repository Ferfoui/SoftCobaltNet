package fr.ferfoui.softcobalt.api.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

public class KeyFileSaver {
    public static void saveKeyToFile(Key key, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key.getEncoded());
        fos.close();
    }
}
