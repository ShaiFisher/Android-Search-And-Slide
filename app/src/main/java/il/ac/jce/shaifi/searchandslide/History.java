package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fisher on 14/02/2016.
 */
public class History {

    private static final String PREF_NAME = "History";
    private static final String QUERIES_NAME = "Query"; // Query1, Query2, ...
    private static final String SIZE_NAME = "size";
    private static final int MAX_SIZE = 10;

    private SharedPreferences preferences;
    private ArrayList<String> queries;

    public History(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int size = preferences.getInt(SIZE_NAME, 0);
        queries = new ArrayList<String>();
        for (int i=0; i<size; i++) {
            String query = preferences.getString(QUERIES_NAME + i, "");
            if (!query.isEmpty()) {
                queries.add(query);
            }
        }
        Utils.log("history (" + size + "):", queries.toString());
    }

    public String getLastQuery() {
        if (queries.isEmpty()) {
            return "";
        } else {
            return queries.get(0);
        }
    }

    public List<String> getHistory() {
        return queries;
    }

    public void add(String query) {
        if (!query.equals(getLastQuery())) {
            queries.add(0, query);
            for(int i=queries.size()-1; i>0; i--) {
                if (queries.get(i).equals(query)) {
                    queries.remove(i);
                }
            }
        }
    }

    public void save() {
        SharedPreferences.Editor editor = preferences.edit();

        int size = Math.min(queries.size(), MAX_SIZE);
        editor.putInt(SIZE_NAME, size);

        for (int i=0; i<size; i++) {
            editor.putString(QUERIES_NAME + i, queries.get(i));
        }
        editor.apply();
    }
}
