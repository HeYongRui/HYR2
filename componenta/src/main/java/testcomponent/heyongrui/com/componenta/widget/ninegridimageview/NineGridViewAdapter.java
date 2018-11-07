package testcomponent.heyongrui.com.componenta.widget.ninegridimageview;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by lambert on 2018/11/6.
 */

public abstract class NineGridViewAdapter<T> {
    protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

    protected void onItemImageClick(Context context, ImageView imageView, int index, List<T> list) {
    }

    protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list) {
        return false;
    }

    protected ImageView generateImageView(Context context) {
        GridImageView imageView = new GridImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
