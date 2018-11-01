package testcomponent.heyongrui.com.base.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import testcomponent.heyongrui.com.base.R;
import testcomponent.heyongrui.com.base.config.glide.GlideApp;
import testcomponent.heyongrui.com.base.config.glide.GlideImageLoader;
import testcomponent.heyongrui.com.base.config.glide.OnProgressListener;

/**
 * Created by lambert on 2018/10/26.
 */

public class GlideUtil {

    public static void load(Context context, Object resource, ImageView imageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_fail)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            GlideApp.with(context).load(resource).apply(options).into(imageView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void loadCircle(Context context, Object resource, ImageView imageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_fail)
                    .dontAnimate()
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            GlideApp.with(context).load(resource).apply(options).into(imageView);
        } catch (IllegalArgumentException e) {
        }
    }

    public static void loadWithProgress(Object resource, ImageView imageView, OnProgressListener onProgressListener) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.gray)
                .error(R.drawable.placeholder_fail)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideImageLoader glideImageLoader = GlideImageLoader.create(imageView);
        glideImageLoader.getGlideRequest().apply(options);
        glideImageLoader.listener(resource, onProgressListener);
        glideImageLoader.loadImage(resource);
    }
}
