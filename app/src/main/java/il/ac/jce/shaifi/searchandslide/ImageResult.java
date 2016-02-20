package il.ac.jce.shaifi.searchandslide;

/**
 * Created by fisher on 10/02/2016.
 * this class is for representing an image object with all its properties
 */
public class ImageResult {

    private String url;
    private String thumbnailUrl;
    private String title;

    public ImageResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String url) {
        thumbnailUrl = url;
    }

    public boolean hasThumbnail() { return (thumbnailUrl != null && !thumbnailUrl.isEmpty()); }
}
