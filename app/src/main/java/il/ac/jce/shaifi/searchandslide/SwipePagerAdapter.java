package il.ac.jce.shaifi.searchandslide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.*;
import java.util.List;

/**
 * Created by itais on 16-Feb-16.
 * this class is to enable image swipes of a list of images
 */

public class SwipePagerAdapter extends PagerAdapter{

    private List<ImageResult> mImages;
    private Context mContext;
    private LayoutInflater layoutInflater;

    // constructor that gets the activity context for doing visual stuff
    // and image list to swipe through the images
    public SwipePagerAdapter(Context context, List<ImageResult> images) {
        this.mContext = context;
        this.mImages = images;
    }

    // return number of images
    @Override
    public int getCount() {
        return mImages.size();
    }

    // return if view is from the object
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    // this method is triggered with every swipe - to get the next image
    @Override
    public View instantiateItem(ViewGroup container, int position) {

        // get layout to display image on
        layoutInflater = ((Activity)mContext).getLayoutInflater();
        View viewItem = layoutInflater.inflate(R.layout.image_swipe, container, false);
        // get the image component
        final ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
        // get the text title component
        TextView txtImageTitle = (TextView) viewItem.findViewById(R.id.txtImageTitle);
        // get the text counter component
        TextView txtImagePlacement = (TextView) viewItem.findViewById(R.id.txtImagePlacement);

        // get the image
        final ImageResult imageResult = mImages.get(position);

        // generate the counter string
        String strImagePlacement = (position + 1) + " / " + mImages.size();
        // put text on the layout
        txtImagePlacement.setText(strImagePlacement);
        txtImageTitle.setText(imageResult.getTitle());

        // download the image with the picasso library
        if (imageResult.hasThumbnail()) {
            Picasso.with(mContext)
                    .load(imageResult.getThumbnailUrl())    // first load the thumbnail to display quick
                    .placeholder(R.drawable.placeholder)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(imageResult.getUrl()) // after thumbnail loaded - load the actual image
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);
                        }

                        @Override
                        public void onError() {
                            Utils.displayMessage(mContext, mContext.getString(R.string.message_load_image_fail));
                        }
                    });
        }
        else {
            Picasso.with(mContext)
                    .load(imageResult.getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }

        // add the image view into the given container
        ((ViewGroup) container).addView(viewItem);

        return viewItem;
    }

    // override method to destroy the pager
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}




