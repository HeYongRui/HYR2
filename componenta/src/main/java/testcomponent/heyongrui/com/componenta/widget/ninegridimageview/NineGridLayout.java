package testcomponent.heyongrui.com.componenta.widget.ninegridimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import testcomponent.heyongrui.com.base.config.glide.GlideApp;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/19.
 */

public class NineGridLayout extends BaseNineGridLayout<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> {
    protected final float MAX_HW_RATIO = 3f;
    protected final float MAX_WIDTH_PERCENT = 0.7f;

    public NineGridLayout(Context context) {
        super(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, MonoTeaDto.EntityListBean.MeowBean.ThumbBean thumbBean, final int parentWidth) {
        if (thumbBean == null) return true;
        String raw = thumbBean.getRaw();
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_fail)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).asBitmap().load(raw).thumbnail(0.5f).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                int oldW = bitmap.getWidth();
                int oldH = bitmap.getHeight();
                float old_hw_ratio = (float) oldH / oldW;
                int newW;
                int newH;
//                //最大宽高比限制单张图片
//                if (old_hw_ratio > MAX_HW_RATIO) {//h:w = 5:3
//                    newW = parentWidth / 2;
//                    newH = newW * 5 / 3;
//                } else if (oldH < oldW) {//h:w = 2:3
//                    newW = parentWidth * 2 / 3;
//                    newH = newW * 2 / 3;
//                } else {//newH:h = newW :w
//                    newW = parentWidth / 2;
//                    newH = oldH * newW / oldW;
//                }
                //自定义最大宽度的自适应单张图片
                int maxWidth = (int) (parentWidth * MAX_WIDTH_PERCENT);
                newW = oldW >= maxWidth ? maxWidth : oldW;
                newH = (int) (newW * old_hw_ratio);
                imageView.setImageBitmap(bitmap);
                setOneImageLayoutParams(imageView, newW, newH);
            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, MonoTeaDto.EntityListBean.MeowBean.ThumbBean thumbBean) {
        if (thumbBean == null) return;
        String raw = thumbBean.getRaw();
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_fail)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideApp.with(mContext).load(raw).thumbnail(0.5f).apply(options).into(imageView);
    }
}
