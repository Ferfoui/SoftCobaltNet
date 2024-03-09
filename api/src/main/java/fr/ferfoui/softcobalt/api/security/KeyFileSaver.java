package fr.ferfoui.softcobalt.api.cryptography;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

public class KeyFileSaver {
    public static void saveKeyToFile(Key key) {
        try (FileOutputStream fos = new FileOutputStream("public.key")) {
            fos.write(key.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
