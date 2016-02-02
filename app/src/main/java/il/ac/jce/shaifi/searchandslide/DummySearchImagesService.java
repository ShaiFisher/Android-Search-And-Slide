package il.ac.jce.shaifi.searchandslide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fisher on 27/01/2016.
 */
public class DummySearchImagesService implements SearchImagesService {

    @Override
    public void searchImages(String query, SearchImagesHandler handler) {
        Utils.log("searchImages:", query);
        List<String> imagesUrls = new ArrayList<String>();
        imagesUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/fb/Big_Sur_Snake.JPG");
        imagesUrls.add("https://www.burkemuseum.org/previous-site-inclusions/images/photos/9124/thamnophis_elegans_todd_w_pierson__large.jpeg");
        imagesUrls.add("https://upload.wikimedia.org/wikipedia/commons/7/76/Coast_Garter_Snake.jpg");
        imagesUrls.add("http://www.wikihow.com/images/8/82/Catch-a-Snake-Step-24.jpg");
        imagesUrls.add("http://res.cloudinary.com/dk-find-out/image/upload/q_80,w_1440/DCTM_Penguin_UK_DK_AL327008_llkmui.png");
        imagesUrls.add("http://images3.alphacoders.com/123/123977.jpg");
        imagesUrls.add("http://cdn.playbuzz.com/cdn/4c8e2feb-d893-447a-8767-3b7a8c5540c3/e1c9fe35-3536-4254-80d2-8549675a088f.jpg");
        handler.handleImagesList(imagesUrls);
    }
}
