package il.ac.jce.shaifi.searchandslide;

import java.util.List;

import javax.security.auth.callback.*;

/**
 * Created by fisher on 27/01/2016.
 */
public interface SearchImagesService {

    public void searchImages(String query, SearchImagesHandler callBack);
}
