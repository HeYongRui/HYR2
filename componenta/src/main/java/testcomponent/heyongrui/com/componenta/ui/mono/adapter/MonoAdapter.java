package testcomponent.heyongrui.com.componenta.ui.mono.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.base.util.UiUtil;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoAdapter extends BaseMultiItemQuickAdapter<MonoMultipleItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MonoAdapter(List<MonoMultipleItem> data) {
        super(data);
        addItemType(MonoMultipleItem.TYPE_ONE, R.layout.adapter_item_monotea_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MonoMultipleItem item) {
        switch (helper.getItemViewType()) {
            case MonoMultipleItem.TYPE_ONE://列表
                UiUtil.setOnclickFeedBack(mContext, R.color.white, R.color.gray, helper.itemView);
                MonoTeaDto.EntityListBean entityListBean = item.getEntityListBean();
                if (entityListBean == null) return;
                ImageView avatarIv = helper.getView(R.id.avatar_iv);
                TextView nameTv = helper.getView(R.id.name_tv);
                TextView wxLinkTv = helper.getView(R.id.wx_link_tv);
                ImageView thumbIv = helper.getView(R.id.thumb_iv);
                TextView textTv = helper.getView(R.id.text_tv);
                TextView titleTv = helper.getView(R.id.title_tv);
                TextView subTitleTv = helper.getView(R.id.sub_title_tv);
                MonoTeaDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
                if (meow == null) return;
                MonoTeaDto.EntityListBean.MeowBean.GroupBean group = meow.getGroup();
                if (group != null) {
                    String logo_url = group.getLogo_url();
                    String name = group.getName();
                    GlideUtil.load(mContext, logo_url, avatarIv);
                    nameTv.setText(name);
                }
                MonoTeaDto.EntityListBean.MeowBean.ThumbBean thumb = meow.getThumb();
                if (thumb != null) {
                    String raw = thumb.getRaw();
                    float aspectRatio = (float) thumb.getWidth() / thumb.getHeight();
                    ConstraintLayout itemRoot = (ConstraintLayout) helper.itemView;
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(itemRoot);
                    constraintSet.setDimensionRatio(thumbIv.getId(), aspectRatio + "");
                    constraintSet.applyTo(itemRoot);
                    if (raw.contains("gif")) {
                        GlideUtil.loadGif(mContext, raw, thumbIv);
                    } else {
                        GlideUtil.load(mContext, raw, thumbIv);
                    }
                }
                String text = meow.getText();
                if (TextUtils.isEmpty(text)) {
                    textTv.setVisibility(View.GONE);
                } else {
                    textTv.setText(text);
                    textTv.setVisibility(View.VISIBLE);
                }
                String title = meow.getTitle();
                if (TextUtils.isEmpty(title)) {
                    titleTv.setVisibility(View.GONE);
                } else {
                    titleTv.setText(title);
                    titleTv.setVisibility(View.VISIBLE);
                }
                String description = meow.getDescription();
                if (TextUtils.isEmpty(description)) {
                    subTitleTv.setVisibility(View.GONE);
                } else {
                    subTitleTv.setText(description);
                    subTitleTv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
