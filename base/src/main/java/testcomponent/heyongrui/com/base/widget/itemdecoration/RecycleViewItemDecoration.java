package testcomponent.heyongrui.com.base.widget.itemdecoration;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import testcomponent.heyongrui.com.base.R;

/**
 * Created by lambert on 2018/3/7.
 */

public class RecycleViewItemDecoration extends BaseItemDecoration {
    private Context mContext;
    private float mWidthDp = -1;
    private int mColor;

    public RecycleViewItemDecoration(Context context) {
        this(context, 10);
    }

    public RecycleViewItemDecoration(Context context, float widthDp) {
        this(context, widthDp, R.color.window_background);
    }

    public RecycleViewItemDecoration(Context context, float widthDp, @ColorRes int color) {
        super(context);
        this.mContext = context;
        this.mWidthDp = widthDp;
        mColor = ContextCompat.getColor(context, color);
    }

    @Override
    public Divider getDivider(int itemPosition) {
//        Divider divider = null;
//        switch (itemPosition % 2) {
//            case 0:
//                //每一行第一个显示rignt和bottom
//                divider = new DividerBuilder()
//                        .setRightSideLine(true, 0xff666666, 10, 0, 0)
//                        .setBottomSideLine(true, 0xff666666, 20, 0, 0)
//                        .create();
//                break;
//            case 1:
//                //第二个显示Left和bottom
//                divider = new DividerBuilder()
//                        .setLeftSideLine(true, 0xff666666, 10, 0, 0)
//                        .setBottomSideLine(true, 0xff666666, 20, 0, 0)
//                        .create();
//                break;
//            default:
//                break;
//        }
        DividerBuilder dividerBuilder = new DividerBuilder();
        if (mWidthDp >= 0) {
            dividerBuilder.setBottomSideLine(true, mColor, mWidthDp, 0, 0);
        } else {
            dividerBuilder.setBottomSideLine(true, mColor, 10, 0, 0);
        }
        Divider divider = dividerBuilder.create();
        return divider;
    }
}