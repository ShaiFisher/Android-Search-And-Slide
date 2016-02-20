package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by fisher on 01/01/2016.
 */
public class Utils {

    // methods to write to console
    public static void log(String name, int value) { log(name, String.valueOf(value)); }
    public static void log(String name, String value) {
        log(name + " " + value);
    }
    public static void log(String str) {
        Log.d("FinalProjectLog", str);
    }
    public static void log(String name, Exception e) { log(name, "exception:" + e.getMessage() + '\n' + e.getStackTrace().toString()); }

    // display a toast from the given message
    public static void displayMessage(Context context, String strErr) {
        CharSequence text = strErr;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
