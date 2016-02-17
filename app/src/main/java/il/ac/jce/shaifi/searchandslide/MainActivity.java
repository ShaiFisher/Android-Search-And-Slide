package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchImagesHandler {

    private Button btnSearch;
    private ViewPager viewPager;
    private AutoCompleteTextView txtQuery;
    private SearchImagesService searchImagesService;
    private List<ImageResult> imageResults;
    private History history;
    private ArrayAdapter<String> historyAdapter;
    private SwipePageAdapter mSwipePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SearchImagesHandler self = this;
        setContentView(R.layout.activity_main);

        Utils.log("onCreate");

        //searchImagesService = new DummySearchImagesService(this, this);
        //searchImagesService = new GoogleSearchImageService(this, this);
        searchImagesService = new BingSearchImageService(this, this);
        history = new History(this);

        btnSearch = (Button) findViewById(R.id.btn_search);
        txtQuery = (AutoCompleteTextView)findViewById(R.id.txt_query);

        historyAdapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, history.getHistory());
        txtQuery.setAdapter(historyAdapter);
        txtQuery.setTextColor(Color.WHITE);

        // set the pager adapter to swipe images
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     // hide keyboard
                     InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(txtQuery.getWindowToken(), 0);

                     String query = txtQuery.getText().toString();
                     // search
                     searchImagesService.searchImages(query);
                     // add to history
                     history.add(query);
                     historyAdapter.insert(query, 0);
                 }
             }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        history.save();
    }

    @Override
    public void handleImagesList(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
        mSwipePager = new SwipePageAdapter(MainActivity.this, this.imageResults);
        viewPager.setAdapter(mSwipePager);

        Utils.log("results recieved:", imageResults.size());
    }
}
