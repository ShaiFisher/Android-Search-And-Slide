package il.ac.jce.shaifi.searchandslide;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchImagesHandler {

    private EditText txtQuery;
    private Button btnSearch;
    private ImageView imageView;
    //private Bitmap image;
    private SearchImagesService searchImagesService;
    private List<String> imagesUrls;
    private int currentImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SearchImagesHandler self = this;
        setContentView(R.layout.activity_main);
        Utils.log("onCreate");
        imageView = (ImageView) findViewById(R.id.imageView);
        searchImagesService = new DummySearchImagesService();

        txtQuery = (EditText) findViewById(R.id.txt_query);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     searchImagesService.searchImages(txtQuery.getText().toString(), self);
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
    public void handleImagesList(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
        currentImageIndex = 0;
        Utils.log("results recieved:", imagesUrls.size());
        displayCurrentImage();
    }

    private void displayCurrentImage() {
        if (currentImageIndex == -1) {
            currentImageIndex = imagesUrls.size() - 1;
        }
        if (currentImageIndex == imagesUrls.size()) {
            currentImageIndex = 0;
        }
        String imageUrl = imagesUrls.get(currentImageIndex);
        Utils.log("loading image:", imageUrl);
        Picasso.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(imageView);

        // preload next image
        if (currentImageIndex + 1 < imagesUrls.size()) {
            Picasso.with(this)
                    .load(imagesUrls.get(currentImageIndex + 1))
                    .fetch();
        }
    }
}
