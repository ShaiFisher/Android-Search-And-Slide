package il.ac.jce.shaifi.searchandslide;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itais on 08-Feb-16.
 * this class implements image search on the google search engine
 * gets a search string and returns an ImageResult array with the search results
 */
public class GoogleSearchImageService  implements SearchImagesService{

    //http request queue
    private RequestQueue mHttpRequestQueue;

    // url of the google web service
    private final String mUrlImageSearch = "https://www.googleapis.com/customsearch/v1";

    // api key to use with the search web service
    private final String mApiKey = "AIzaSyAhiU0VSynjFE9xfXsOS64zxyq-dziNsSM";
    private final String mSearchEngineId = "017530607855097223459:gx-1qpn0cyi";

    private List<ImageResult> mImageUrls;
    private SearchImagesHandler mImageHandler;
    private Context mContext;

    // constructor that gets the activity context for doing visual stuff
    // and SearchImagesHandler to send the image list through
    public GoogleSearchImageService(Context context, SearchImagesHandler handler){
        mImageUrls = new ArrayList<ImageResult>();
        mImageHandler = handler;
        mContext = context;
        mHttpRequestQueue = Volley.newRequestQueue(mContext);
    }

    // override this function so we can run it here in this class
    @Override
    public void searchImages(String strQuery) {
        String webServiceUrl = generateUrl(strQuery);

        getNewImageList(webServiceUrl);
    }

    // generate the url to send request to
    private String generateUrl(String strQuery) {
        // remove all spaces from search string
        strQuery = strQuery.trim();

        // encode the search to be good as a url string
        strQuery = Uri.encode(strQuery);

        String url = mUrlImageSearch;
        url += "?key=" + mApiKey;
        url += "&cx=" + mSearchEngineId;
        url += "&searchType=image";
        url += "&q=" + strQuery;
        return url;
    }

    // generate image list from given url
    private void getNewImageList(String url) {

        // generate the web service request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                // start response listener to wait for a good response
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // check if got any images
                            JSONObject obj = response.getJSONObject("searchInformation");
                            int imagesNumber = obj.getInt("totalResults");
                            if (imagesNumber == 0)
                                Utils.displayMessage(mContext, mContext.getString(R.string.message_found_no_images));
                            else {
                                // 1. get the list of all images
                                JSONArray arrImages = response.getJSONArray("items");
                                // 2. generate url string array from the json array of image items
                                mImageUrls = getImagesFromJSON(arrImages);
                                // 3. display all images on activity
                                mImageHandler.handleImagesList(mImageUrls);
                                // 4. display toast with returned images found
                                Utils.displayMessage(mContext, mContext.getString(R.string.message_web_service_return_number) + " " + mImageUrls.size());
                            }
                        }
                        catch (JSONException ex){
                            // stop the wait dialog if exception occured
                            Utils.displayMessage(mContext, mContext.getString(R.string.message_web_service_failed));
                        }
                    }
                },
                // start listener for an error on the request
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // stop the wait dialog if response has errors
                        Utils.displayMessage(mContext, mContext.getString(R.string.message_web_service_failed));
                    }
                }
        );

        // add the request to the http queue
        mHttpRequestQueue.add(request);

    }


    // extract a string array of urls from the json
    private ArrayList<ImageResult> getImagesFromJSON(JSONArray imageArray) throws JSONException{

        JSONObject obj;
        ArrayList<ImageResult> items = new ArrayList<ImageResult>();
        GoogleImage item;

        // go over all json image items given
        for (int i = 0; i < imageArray.length(); i++) {

            // get the specific object
            obj = imageArray.getJSONObject(i);
            try{
                // convert json into a googleItem object
                Gson gson = new GsonBuilder().create();
                // convert the GoogleImage object to our ImageResult object to be returned (if url exist)
                item = gson.fromJson(obj.toString(), GoogleImage.class);
                if (item.getImageUrl() != null) {
                    ImageResult imageResult = new ImageResult(item.getImageUrl());
                    imageResult.setTitle(item.getTitle());
                    imageResult.setThumbnailUrl(item.image.thumbnailLink);
                    items.add(imageResult);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        // return the result image array
        return items;
    }

    // inner class to hold a google image object
    private class GoogleImage {

        private String kind;
        private String title;
        private String htmlTitle;
        private String link;
        private String displayLink;
        private String snippet;
        private String htmlSnippet;
        private String mime;
        private String fileFormat;
        private ImageObj image;

        public String getImageUrl() {
            if (link != null)
                return link;
            else
                return null;
        }

        public String getTitle() {
            return title;
        }

        private class ImageObj {

            private String contextLink;
            private int height;
            private int width;
            private int byteSize;
            private String thumbnailLink;
            private int thumbnailHeight;
            private int thumbnailWidth;
        }
    }
}
