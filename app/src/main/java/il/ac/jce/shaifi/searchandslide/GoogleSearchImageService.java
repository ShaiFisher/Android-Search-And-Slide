package il.ac.jce.shaifi.searchandslide;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
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

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by itais on 08-Feb-16.
 */
public class GoogleSearchImageService  implements SearchImagesService{

    //http request queue
    private RequestQueue mHttpRequestQueue;

    // url of the google web service
    // private final String mUrlImageSearch = "https://www.googleapis.com/customsearch/v1?key=AIzaSyAhiU0VSynjFE9xfXsOS64zxyq-dziNsSM&cx=017530607855097223459:gx-1qpn0cyi&q=%D7%91%D7%99%D7%91%D7%99&searchType=imaget";
    private final String mUrlImageSearch = "https://www.googleapis.com/customsearch/v1";
          //  "?key=&cx=&q=Type=image";
    // api key to use with the search web service
    private final String mApiKey = "AIzaSyAhiU0VSynjFE9xfXsOS64zxyq-dziNsSM";
    private final String mSearchEngineId = "017530607855097223459:gx-1qpn0cyi";

    private List<ImageResult> mImageUrls;
    private SearchImagesHandler mImageHandler;
    private Context mContext;

    public GoogleSearchImageService(Context context, SearchImagesHandler handler){
        mImageUrls = new ArrayList<ImageResult>();
        mImageHandler = handler;
        mContext = context;
        mHttpRequestQueue = Volley.newRequestQueue(mContext);
    }
    @Override
    public void searchImages(String strQuery) {
        Utils.log("GoogleSearchImageService for query:", strQuery);

        String webServiceUrl = generateUrl(strQuery);

        getNewImageList(webServiceUrl);

    }

    // generate the url to send request to
    private String generateUrl(String strQuery) {
        String url = mUrlImageSearch;
        url += "?key=" + mApiKey;
        url += "&cx=" + mSearchEngineId;
        url += "&searchType=image";
        url += "&q=" + Uri.encode(strQuery);
        Utils.log("GoogleSearchImageService start url:", url);
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
                            Utils.log("GoogleSearchImageService got json:", response.toString());

                            // check if got any images
                            JSONObject obj = response.getJSONObject("searchInformation");
                            int imagesNumber = obj.getInt("totalResults");
                            if (imagesNumber == 0)
                                displayError("found no images for this search");
                            else {
                                // 1. get the list of all images
                                JSONArray arrImages = response.getJSONArray("items");
                                // 2. generate url string array from the json array of image items
                                mImageUrls = getImagesFromJSON(arrImages);
                                // 3. display all images on activity
                                mImageHandler.handleImagesList(mImageUrls);
                            }
                        }
                        catch (JSONException ex){
                            // stop the wait dialog if exception occured
                            displayError("image web service failed: " + ex.getMessage());
                        }
                    }
                },
                // start listener for an error on the request
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // stop the wait dialog if response has errors
                        displayError("image web service failed to respond: " + error.getMessage());
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
                Gson gson = new GsonBuilder().create();
                Utils.log("GoogleSearchImageService deserializing json: ", obj.toString());
                item = gson.fromJson(obj.toString(), GoogleImage.class);
                Utils.log("GoogleSearchImageService deserialized json to GoogleImage: ", item.toString());
                if (item.getImageUrl() != null) {
                    ImageResult imageResult = new ImageResult(item.getImageUrl());
                    imageResult.setTitle(item.getTitle());
                    imageResult.setThumbnailUrl(item.image.thumbnailLink);
                    items.add(imageResult);
                }
                else
                    Utils.log("Empty image url");
            }
            catch(Exception e) {
                Utils.log("GoogleSearchImageService", e);
            }
        }

        Utils.log("GoogleSearchImageService returned url list: ", items.toString());

        // return the result weather array
        return items;
    }



    // display a toast from the given error
    private void displayError(String strErr) {

        CharSequence text = strErr;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(mContext, text, duration);
        toast.show();
    }


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
