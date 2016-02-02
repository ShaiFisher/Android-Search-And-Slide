package il.ac.jce.shaifi.searchandslide;

import android.util.Log;

/**
 * Created by fisher on 01/01/2016.
 */
public class Utils {

    public static void log(String name, int value) {
        log(name, String.valueOf(value));
    }

    public static void log(String name, String value) {
        log(name + " " + value);
    }
    public static void log(String str) {
        Log.d("Ex3Log", str);
    }

    public static void log(String name, Exception e) {
        log(name, "exception:" + e.getMessage() + '\n' + e.getStackTrace().toString());
    }
}
