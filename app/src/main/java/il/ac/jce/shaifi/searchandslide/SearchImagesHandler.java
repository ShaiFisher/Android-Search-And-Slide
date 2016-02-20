package il.ac.jce.shaifi.searchandslide;

import java.util.List;

/**
 * Created by fisher on 28/01/2016.
 * this class is an interface for a search handler - to send image list back to the main activity
 */
public interface SearchImagesHandler {
    void handleImagesList(List<ImageResult> imageResults);
}
