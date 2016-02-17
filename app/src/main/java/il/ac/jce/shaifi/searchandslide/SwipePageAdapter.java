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
 */

public class SwipePageAdapter extends PagerAdapter{

    private List<ImageResult> mImages;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public SwipePageAdapter(Context context, List<ImageResult> images) {
        this.mContext = context;
        this.mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        layoutInflater = ((Activity)mContext).getLayoutInflater();
        View viewItem = layoutInflater.inflate(R.layout.image_swipe, container, false);
        final ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
        TextView txtImageTitle = (TextView) viewItem.findViewById(R.id.txtImageTitle);
        TextView txtImagePlacement = (TextView) viewItem.findViewById(R.id.txtImagePlacement);

        final ImageResult imageResult = mImages.get(position);

        String strImagePlacement = (position + 1) + " / " + mImages.size();
        txtImagePlacement.setText(strImagePlacement);
        txtImageTitle.setText(imageResult.getTitle());

        if (imageResult.getThumbnailUrl() != null) {
            Picasso.with(mContext)
                    .load(imageResult.getThumbnailUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(imageResult.getUrl())
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
        else {
            Picasso.with(mContext)
                    .load(imageResult.getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }

        ((ViewGroup) container).addView(viewItem);

        return viewItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}




