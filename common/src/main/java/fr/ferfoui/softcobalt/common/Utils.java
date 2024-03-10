package fr.ferfoui.softcobalt.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isBase64(String stringToCheck) {
        String strPattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(stringToCheck);

        return matcher.find();
    }

}
