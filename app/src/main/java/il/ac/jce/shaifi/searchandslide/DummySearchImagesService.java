package il.ac.jce.shaifi.searchandslide;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fisher on 27/01/2016.
 * this class is a dummy search image class to be used as mock data
 */
public class DummySearchImagesService implements SearchImagesService {

    private SearchImagesHandler mImageHandler;

    // constructor that gets the activity context for doing visual stuff
    // and SearchImagesHandler to send the image list through
    public DummySearchImagesService(Context context, SearchImagesHandler handler){
        mImageHandler = handler;
    }

    // override this function so we can run it here in this class
    @Override
    public void searchImages(String query) {
        Utils.log("searchImages:", query);
        List<ImageResult> imageResults = new ArrayList<ImageResult>();
        imageResults.add(generateSearchResult("https://upload.wikimedia.org/wikipedia/commons/f/fb/Big_Sur_Snake.JPG"));
        imageResults.add(generateSearchResult("https://www.burkemuseum.org/previous-site-inclusions/images/photos/9124/thamnophis_elegans_todd_w_pierson__large.jpeg"));
        imageResults.add(generateSearchResult("https://upload.wikimedia.org/wikipedia/commons/7/76/Coast_Garter_Snake.jpg"));
        imageResults.add(generateSearchResult("http://www.wikihow.com/images/8/82/Catch-a-Snake-Step-24.jpg"));
        imageResults.add(generateSearchResult("http://res.cloudinary.com/dk-find-out/image/upload/q_80,w_1440/DCTM_Penguin_UK_DK_AL327008_llkmui.png"));
        imageResults.add(generateSearchResult("http://images3.alphacoders.com/123/123977.jpg"));
        imageResults.add(generateSearchResult("http://cdn.playbuzz.com/cdn/4c8e2feb-d893-447a-8767-3b7a8c5540c3/e1c9fe35-3536-4254-80d2-8549675a088f.jpg"));
        mImageHandler.handleImagesList(imageResults);
    }

    private ImageResult generateSearchResult(String url) {
        ImageResult imageResult = new ImageResult(url);
        imageResult.setTitle("This is a description of the image");
        return imageResult;
    }
}
