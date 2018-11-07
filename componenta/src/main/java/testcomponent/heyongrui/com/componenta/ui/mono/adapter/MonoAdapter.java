package testcomponent.heyongrui.com.componenta.ui.mono.adapter;

import android.content.Context;
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
import testcomponent.heyongrui.com.componenta.widget.ninegridimageview.GridImageView;
import testcomponent.heyongrui.com.componenta.widget.ninegridimageview.NineGridView;
import testcomponent.heyongrui.com.componenta.widget.ninegridimageview.NineGridViewAdapter;

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
        addItemType(MonoMultipleItem.TYPE_ONE, R.layout.adapter_item_monotea_one);
        addItemType(MonoMultipleItem.TYPE_TWO, R.layout.adapter_item_monotea_two);
    }

    @Override
    protected void convert(BaseViewHolder helper, MonoMultipleItem item) {
        UiUtil.setOnclickFeedBack(mContext, R.color.white, R.color.gray, helper.itemView);
        MonoTeaDto.EntityListBean entityListBean = item.getEntityListBean();
        if (entityListBean == null) return;
        MonoTeaDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
        if (meow == null) return;
        MonoTeaDto.EntityListBean.MeowBean.GroupBean group = meow.getGroup();
        switch (helper.getItemViewType()) {
            case MonoMultipleItem.TYPE_ONE:
                ImageView oneAvatarIv = helper.getView(R.id.avatar_iv);
                TextView oneNameTv = helper.getView(R.id.name_tv);
                TextView oneWxLinkTv = helper.getView(R.id.wx_link_tv);
                ImageView oneThumbIv = helper.getView(R.id.thumb_iv);
                TextView oneTextTv = helper.getView(R.id.text_tv);
                TextView oneTitleTv = helper.getView(R.id.title_tv);
                TextView oneSubTitleTv = helper.getView(R.id.sub_title_tv);
                if (group != null) {
                    GlideUtil.load(mContext, group.getLogo_url(), oneAvatarIv);
                    oneNameTv.setText(group.getName());
                    oneWxLinkTv.setText(group.getCategory());
                }
                MonoTeaDto.EntityListBean.MeowBean.ThumbBean thumb = meow.getThumb();
                if (thumb != null) {
                    String raw = thumb.getRaw();
                    float aspectRatio = (float) thumb.getWidth() / thumb.getHeight();
                    ConstraintLayout itemRoot = (ConstraintLayout) helper.itemView;
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(itemRoot);
                    constraintSet.setDimensionRatio(oneThumbIv.getId(), aspectRatio + "");
                    constraintSet.applyTo(itemRoot);
                    if (raw.contains("gif")) {
                        GlideUtil.loadGif(mContext, raw, oneThumbIv);
                    } else {
                        GlideUtil.load(mContext, raw, oneThumbIv);
                    }
                }
                String oneText = meow.getText();
                if (TextUtils.isEmpty(oneText)) {
                    oneTextTv.setVisibility(View.GONE);
                } else {
                    oneTextTv.setText("“" + oneText + "”");
                    oneTextTv.setVisibility(View.VISIBLE);
                }
                String oneTitle = meow.getTitle();
                if (TextUtils.isEmpty(oneTitle)) {
                    oneTitleTv.setVisibility(View.GONE);
                } else {
                    oneTitleTv.setText(oneTitle);
                    oneTitleTv.setVisibility(View.VISIBLE);
                }
                String description = meow.getDescription();
                if (TextUtils.isEmpty(description)) {
                    oneSubTitleTv.setVisibility(View.GONE);
                } else {
                    oneSubTitleTv.setText(description);
                    oneSubTitleTv.setVisibility(View.VISIBLE);
                }
                break;
            case MonoMultipleItem.TYPE_TWO:
                ImageView twoAvatarIv = helper.getView(R.id.avatar_iv);
                TextView twoNameTv = helper.getView(R.id.name_tv);
                TextView twoWxLinkTv = helper.getView(R.id.wx_link_tv);
                TextView twoTitleTv = helper.getView(R.id.title_tv);
                TextView twoTextTv = helper.getView(R.id.text_tv);
                NineGridView twoNinegridview = helper.getView(R.id.ninegridview);

                String twoTitle = meow.getTitle();
                if (TextUtils.isEmpty(twoTitle)) {
                    twoTitleTv.setVisibility(View.GONE);
                } else {
                    twoTitleTv.setText(twoTitle);
                    twoTitleTv.setVisibility(View.VISIBLE);
                }
                String twoText = meow.getText();
                if (TextUtils.isEmpty(twoText)) {
                    twoTextTv.setVisibility(View.GONE);
                } else {
                    twoTextTv.setText(twoText);
                    twoTextTv.setVisibility(View.VISIBLE);
                }
                if (group != null) {
                    GlideUtil.load(mContext, group.getLogo_url(), twoAvatarIv);
                    twoNameTv.setText(group.getName());
                    twoWxLinkTv.setText(group.getCategory());
                }
                List<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> pics = meow.getPics();
                if (pics != null && !pics.isEmpty()) {//设置九宫格
                    twoNinegridview.setAdapter(getNineGridAdapter(MonoTeaDto.EntityListBean.MeowBean.ThumbBean.class));
                    twoNinegridview.setImagesData(pics);
                }
                break;
        }
    }

    private <T> NineGridViewAdapter<T> getNineGridAdapter(T t) {
        NineGridViewAdapter<T> adapter = new NineGridViewAdapter<T>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, T t) {
                if (t instanceof MonoTeaDto.EntityListBean.MeowBean.ThumbBean) {
                    String raw = ((MonoTeaDto.EntityListBean.MeowBean.ThumbBean) t).getRaw();
                    if (raw.contains("gif")) {
                        GlideUtil.loadGif(context, raw, imageView);
                    } else {
                        GlideUtil.load(context, raw, imageView);
                    }
                }
            }

            @Override
            protected ImageView generateImageView(Context context) {
                GridImageView imageView = new GridImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }
        };
        return adapter;
    }
}
