package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fisher on 14/02/2016.
 * this class handles history of searches
 */
public class History {

    private static final String PREF_NAME = "History";
    private static final String QUERIES_NAME = "Query"; // Query1, Query2, ...
    private static final String SIZE_NAME = "size";
    private static final int MAX_SIZE = 10;

    // SharedPreferences to save the history into
    private SharedPreferences preferences;
    private ArrayList<String> queries;

    // constructor that gets the context of the activity to get the SharedPreferences from it
    public History(Context context) {

        // get prefs from the context
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // get the amount of history
        int size = preferences.getInt(SIZE_NAME, 0);
        queries = new ArrayList<String>();

        // create a query aaray from the saves prefs
        for (int i=0; i<size; i++) {
            String query = preferences.getString(QUERIES_NAME + i, "");
            if (!query.isEmpty())
                queries.add(query);
        }
    }

    // method to return the last search query that was entered
    public String getLastQuery() {

        if (queries.isEmpty())
            return "";
        else
            return queries.get(0);
    }

    // return the query array
    public List<String> getHistory() {
        return queries;
    }

    // add a search query to the query array
    public void add(String query) {
        // remove the search if exist allready
        queries.remove(query);
        // then add the search as the first one
        queries.add(0, query);
    }

    // save the current search array to the SharedPreferences
    public void save() {
        SharedPreferences.Editor editor = preferences.edit();

        int size = Math.min(queries.size(), MAX_SIZE);
        editor.putInt(SIZE_NAME, size);

        for (int i=0; i<size; i++)
            editor.putString(QUERIES_NAME + i, queries.get(i));

        editor.apply();
    }
}
