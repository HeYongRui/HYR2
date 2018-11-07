package testcomponent.heyongrui.com.componenta.widget.ninegridimageview;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by lambert on 2018/11/6.
 */

public interface ItemImageLongClickListener<T> {
    boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list);
}