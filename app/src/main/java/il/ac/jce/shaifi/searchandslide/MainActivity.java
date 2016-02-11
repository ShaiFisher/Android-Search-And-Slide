package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchImagesHandler {

    private EditText txtQuery;
    private Button btnSearch;
    private ImageView imageView;
    private TextView txtImageInfo;
    //private Bitmap image;
    private SearchImagesService searchImagesService;
    private List<ImageResult> imageResults;
    private int currentImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SearchImagesHandler self = this;
        setContentView(R.layout.activity_main);
        Utils.log("onCreate");

        //searchImagesService = new DummySearchImagesService(this, this);
        //searchImagesService = new GoogleSearchImageService(this, this);
        searchImagesService = new BingSearchImageService(this, this);

        imageView = (ImageView) findViewById(R.id.imageView);
        txtQuery = (EditText) findViewById(R.id.txt_query);
        btnSearch = (Button) findViewById(R.id.btn_search);
        txtImageInfo = (TextView) findViewById(R.id.txt_imageInfo);
        btnSearch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     // hide keyboard
                     InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(txtQuery.getWindowToken(), 0);
                     // search
                     searchImagesService.searchImages(txtQuery.getText().toString());
                 }
             }
        );

        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                Utils.log("onSwipeRight");
                currentImageIndex++;
                displayCurrentImage();
            }

            public void onSwipeLeft() {
                Utils.log("onSwipeLeft");
                currentImageIndex--;
                displayCurrentImage();
            }
        });


    }


    @Override
    public void handleImagesList(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
        currentImageIndex = 0;
        Utils.log("results recieved:", imageResults.size());
        displayCurrentImage();
    }

    private void displayCurrentImage() {
        if (currentImageIndex == -1) {
            currentImageIndex = imageResults.size() - 1;
        }
        if (currentImageIndex == imageResults.size()) {
            currentImageIndex = 0;
        }
        ImageResult imageResult = imageResults.get(currentImageIndex);

        txtImageInfo.setText((currentImageIndex+1) + " / " + imageResults.size());
        if (imageResult.getThumbnailUrl() != null) {
            Picasso.with(this)
                    .load(imageResult.getThumbnailUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }

        Utils.log("loading image:", imageResult.getUrl());
        Picasso.with(this)
            .load(imageResult.getUrl())
            .placeholder(R.drawable.placeholder)
            .into(imageView);

        // preload next image
        if (currentImageIndex + 1 < imageResults.size()) {
            Picasso.with(this)
                    .load(imageResults.get(currentImageIndex + 1).getUrl())
                    .fetch();
        }
    }
}
