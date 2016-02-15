package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.*;
import com.squareup.picasso.Callback;

import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchImagesHandler {

    //private EditText txtQuery;
    private Button btnSearch;
    private ImageView imageView;
    private TextView txtImageInfo;
    private AutoCompleteTextView txtQuery;
    //private Bitmap image;
    private SearchImagesService searchImagesService;
    private List<ImageResult> imageResults;
    private int currentImageIndex;
    private History history;
    private ArrayAdapter<String> historyAdapter;

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

        imageView = (ImageView) findViewById(R.id.imageView);
        //txtQuery = (EditText) findViewById(R.id.txt_query);
        //txtQuery.setText(history.getLastQuery());
        btnSearch = (Button) findViewById(R.id.btn_search);
        txtImageInfo = (TextView) findViewById(R.id.txt_imageInfo);
        txtQuery = (AutoCompleteTextView)findViewById(R.id.txt_query);

        historyAdapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, history.getHistory());
        txtQuery.setAdapter(historyAdapter);
        txtQuery.setTextColor(Color.WHITE);

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

        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                //Utils.log("onSwipeRight");
                currentImageIndex++;
                displayCurrentImage();
            }

            public void onSwipeLeft() {
                //Utils.log("onSwipeLeft");
                currentImageIndex--;
                displayCurrentImage();
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        history.save();
    }

    @Override
    public void handleImagesList(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
        currentImageIndex = 0;
        Utils.log("results recieved:", imageResults.size());
        displayCurrentImage();
    }

    private void displayCurrentImage() {
        final Context context = this;
        if (currentImageIndex == -1) {
            currentImageIndex = imageResults.size() - 1;
        }
        if (currentImageIndex == imageResults.size()) {
            currentImageIndex = 0;
        }
        final ImageResult imageResult = imageResults.get(currentImageIndex);

        txtImageInfo.setText((currentImageIndex + 1) + " / " + imageResults.size());
        if (imageResult.getThumbnailUrl() != null) {
            //Utils.log("loading thumbnail first:", imageResult.getThumbnailUrl());
            Picasso.with(this)
                    .load(imageResult.getThumbnailUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(context)
                                    .load(imageResult.getUrl())
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);
                        }

                        @Override
                        public void onError() {}
                    });
        } else {
            //Utils.log("loading image:", imageResult.getUrl());
            Picasso.with(this)
                    .load(imageResult.getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }



        // preload next image
        if (currentImageIndex + 1 < imageResults.size()) {
            Picasso.with(this)
                    .load(imageResults.get(currentImageIndex + 1).getUrl())
                    .fetch();
        }
    }
}
