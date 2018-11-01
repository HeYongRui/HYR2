package testcomponent.heyongrui.com.componenta.ui.unsplash.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;

/**
 * Created by lambert on 2018/10/24.
 */

public class UnsplashAdapter extends BaseMultiItemQuickAdapter<UnsplashMultipleItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public UnsplashAdapter(List<UnsplashMultipleItem> data) {
        super(data);
        addItemType(UnsplashMultipleItem.TYPE_ONE, R.layout.adapter_item_unsplash_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnsplashMultipleItem item) {
//        UiUtil.setOnclickFeedBack(mContext, R.color.white, R.color.gray, helper.itemView);
        switch (helper.getItemViewType()) {
            case UnsplashMultipleItem.TYPE_ONE://列表
                ImageView ivTypeOne = helper.getView(R.id.iv);
                UnsplashPicDto unsplashPicDtoTypeOne = item.getUnsplashPicDto();
                if (unsplashPicDtoTypeOne == null) return;
                UnsplashPicDto.UrlsBean urlsTypeOne = unsplashPicDtoTypeOne.getUrls();
                if (urlsTypeOne == null) return;
                String smallTypeOne = urlsTypeOne.getSmall();
                GlideUtil.load(mContext, smallTypeOne, ivTypeOne);
                break;
        }
    }
}
