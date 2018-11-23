package testcomponent.heyongrui.com.componenta.ui.mono.adapter;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import testcomponent.heyongrui.com.base.util.DrawableUtil;
import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.base.util.UiUtil;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.MonoCategoryDto;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTabDto;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;
import testcomponent.heyongrui.com.componenta.widget.ninegridimageview.ItemImageClickListener;
import testcomponent.heyongrui.com.componenta.widget.ninegridimageview.NineGridLayout;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoAdapter extends BaseMultiItemQuickAdapter<MonoMultipleItem, BaseViewHolder> {

    private ItemImageClickListener<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> mItemImageClickListener;

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
        addItemType(MonoMultipleItem.TYPE_CATEGORY, R.layout.adapter_item_monocategory);
        addItemType(MonoMultipleItem.TYPE_TAB_MUSIC, R.layout.adapter_item_mono_music_tab);
    }

    @Override
    protected void convert(BaseViewHolder helper, MonoMultipleItem item) {
        UiUtil.setOnclickFeedBack(mContext, R.color.white, R.color.gray, helper.itemView);
        switch (helper.getItemViewType()) {
            case MonoMultipleItem.TYPE_ONE:
                ImageView oneAvatarIv = helper.getView(R.id.avatar_iv);
                TextView oneNameTv = helper.getView(R.id.name_tv);
                TextView oneCategoryTv = helper.getView(R.id.category_tv);
                helper.addOnClickListener(R.id.category_tv);
                ImageView oneThumbIv = helper.getView(R.id.thumb_iv);
                TextView oneTextTv = helper.getView(R.id.text_tv);
                TextView oneTitleTv = helper.getView(R.id.title_tv);
                TextView oneSubTitleTv = helper.getView(R.id.sub_title_tv);
                MonoTeaDto.EntityListBean oneEntityListBean = item.getEntityListBean();
                if (oneEntityListBean == null) return;
                MonoTeaDto.EntityListBean.MeowBean oneMeow = oneEntityListBean.getMeow();
                if (oneMeow == null) return;
                MonoTeaDto.EntityListBean.MeowBean.GroupBean oneGroup = oneMeow.getGroup();
                if (oneGroup != null) {
                    GlideUtil.load(mContext, oneGroup.getLogo_url(), oneAvatarIv);
                    oneNameTv.setText(oneGroup.getName());
                    oneCategoryTv.setText(oneGroup.getCategory());
                    oneCategoryTv.setTextColor(UiUtil.createTextColorStateList(ContextCompat.getColor(mContext, R.color.colorCyan),
                            ContextCompat.getColor(mContext, R.color.colorPrimaryDark),
                            ContextCompat.getColor(mContext, R.color.colorCyan)));
                }
                MonoTeaDto.EntityListBean.MeowBean.ThumbBean thumb = oneMeow.getThumb();
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
                String oneText = oneMeow.getText();
                if (TextUtils.isEmpty(oneText)) {
                    oneTextTv.setVisibility(View.GONE);
                } else {
                    oneTextTv.setText("“" + oneText + "”");
                    oneTextTv.setVisibility(View.VISIBLE);
                }
                String oneTitle = oneMeow.getTitle();
                if (TextUtils.isEmpty(oneTitle)) {
                    oneTitleTv.setVisibility(View.GONE);
                } else {
                    oneTitleTv.setText(oneTitle);
                    oneTitleTv.setVisibility(View.VISIBLE);
                }
                String description = oneMeow.getDescription();
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
                TextView twoCategoryTv = helper.getView(R.id.category_tv);
                helper.addOnClickListener(R.id.category_tv);
                TextView twoTitleTv = helper.getView(R.id.title_tv);
                TextView twoTextTv = helper.getView(R.id.text_tv);
                NineGridLayout twoNinegridview = helper.getView(R.id.ninegridview);

                MonoTeaDto.EntityListBean twoEntityListBean = item.getEntityListBean();
                if (twoEntityListBean == null) return;
                MonoTeaDto.EntityListBean.MeowBean twoMeow = twoEntityListBean.getMeow();
                if (twoMeow == null) return;
                MonoTeaDto.EntityListBean.MeowBean.GroupBean twoGroup = twoMeow.getGroup();
                String twoTitle = twoMeow.getTitle();
                if (TextUtils.isEmpty(twoTitle)) {
                    twoTitleTv.setVisibility(View.GONE);
                } else {
                    twoTitleTv.setText(twoTitle);
                    twoTitleTv.setVisibility(View.VISIBLE);
                }
                String twoText = twoMeow.getText();
                if (TextUtils.isEmpty(twoText)) {
                    twoTextTv.setVisibility(View.GONE);
                } else {
                    twoTextTv.setText(twoText);
                    twoTextTv.setVisibility(View.VISIBLE);
                }
                if (twoGroup != null) {
                    GlideUtil.load(mContext, twoGroup.getLogo_url(), twoAvatarIv);
                    twoNameTv.setText(twoGroup.getName());
                    twoCategoryTv.setText(twoGroup.getCategory());
                    twoCategoryTv.setTextColor(UiUtil.createTextColorStateList(ContextCompat.getColor(mContext, R.color.colorCyan),
                            ContextCompat.getColor(mContext, R.color.colorPrimaryDark),
                            ContextCompat.getColor(mContext, R.color.colorCyan)));
                }
                List<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> pics = twoMeow.getPics();
                if (pics != null && !pics.isEmpty()) {//设置九宫格
                    twoNinegridview.setImagesData(pics);
                    if (mItemImageClickListener != null) {
                        twoNinegridview.setItemImageClickListener(mItemImageClickListener);
                    }
                }
                break;
            case MonoMultipleItem.TYPE_CATEGORY:
                ImageView categoryThumbIv = helper.getView(R.id.thumb_iv);
                TextView categoryMultipleTv = helper.getView(R.id.multiple_tv);
                TextView categoryTitleTv = helper.getView(R.id.title_tv);
                TextView categoryDesTv = helper.getView(R.id.des_tv);
                TextView categoryNameTv = helper.getView(R.id.name_tv);

                MonoCategoryDto.MeowsBean meowsBean = item.getMeowsBean();
                if (meowsBean != null) {
                    MonoCategoryDto.MeowBean meowBean = meowsBean.getMeow();
                    MonoCategoryDto.MeowBean.ThumbBean categoryThumb = meowBean.getThumb();
                    List<MonoCategoryDto.MeowBean.ThumbBean> images = meowBean.getImages();
                    if (categoryThumb != null) {
                        categoryMultipleTv.setVisibility(View.GONE);
                        String raw = categoryThumb.getRaw();
                        if (raw.contains("gif")) {
                            GlideUtil.loadGif(mContext, raw, categoryThumbIv);
                        } else {
                            GlideUtil.load(mContext, raw, categoryThumbIv);
                        }
                    } else {
                        if (images != null && !images.isEmpty()) {
                            MonoCategoryDto.MeowBean.ThumbBean thumbBean = images.get(0);
                            String raw = thumbBean.getRaw();
                            if (raw.contains("gif")) {
                                GlideUtil.loadGif(mContext, raw, categoryThumbIv);
                            } else {
                                GlideUtil.load(mContext, raw, categoryThumbIv);
                            }
                        }
                    }
                    if (images != null && !images.isEmpty()) {
                        categoryMultipleTv.setVisibility(View.VISIBLE);
                        categoryMultipleTv.setText(images.size() + "张");
                        Drawable drawable = DrawableUtil.tintDrawable(mContext, R.drawable.placeholder, R.color.colorCyan);
                        drawable.setBounds(0, 0, 30, 30);
                        GradientDrawable gradientDrawable = new DrawableUtil.DrawableBuilder(mContext).setColor(R.color.translucentBlack).setGradientRoundRadius(30).createGradientDrawable();
                        categoryMultipleTv.setBackgroundDrawable(gradientDrawable);
                        categoryMultipleTv.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        categoryMultipleTv.setVisibility(View.GONE);
                    }
                    categoryTitleTv.setText(meowBean.getTitle());
                    categoryDesTv.setText(meowBean.getDescription());
                    MonoCategoryDto.MeowBean.GroupBean group = meowBean.getGroup();
                    if (group != null) {
                        categoryNameTv.setText(group.getName());
                    }
                }
                break;
            case MonoMultipleItem.TYPE_TAB_MUSIC:
                ImageView musicAvatarIv = helper.getView(R.id.avatar_iv);
                TextView musicNicknameTv = helper.getView(R.id.nickname_tv);
                TextView musicCategoryTv = helper.getView(R.id.category_tv);
                ImageView musicThumbIv = helper.getView(R.id.thumb_iv);
                ProgressBar musicProgressbar = helper.getView(R.id.progressbar);
                musicProgressbar.setMax(100);
                musicProgressbar.setProgress(0);
                ImageView musicPlayIv = helper.getView(R.id.play_iv);
                musicPlayIv.setImageResource(R.drawable.play);
                helper.addOnClickListener(R.id.play_iv);
                TextView musicSongNameTv = helper.getView(R.id.song_name_tv);
                TextView musicDurationTv = helper.getView(R.id.duration_tv);
                TextView musicTitleTv = helper.getView(R.id.title_tv);
                TextView musicDescTv = helper.getView(R.id.desc_tv);

                MonoTabDto.EntityListBean tabEntityListBean = item.getTabEntityListBean();
                if (tabEntityListBean != null) {
                    MonoTabDto.EntityListBean.MeowBean meow = tabEntityListBean.getMeow();
                    if (meow != null) {
                        MonoTabDto.EntityListBean.MeowBean.GroupBean group = meow.getGroup();
                        if (group != null) {
                            String logo_url = group.getLogo_url();
                            if (logo_url.contains("gif")) {
                                GlideUtil.loadGif(mContext, logo_url, musicAvatarIv);
                            } else {
                                GlideUtil.load(mContext, logo_url, musicAvatarIv);
                            }
                            musicNicknameTv.setText(group.getName());
                            musicCategoryTv.setText(group.getCategory());

                        }
                        MonoTabDto.EntityListBean.MeowBean.ThumbBean musicThumb = meow.getThumb();
                        if (musicThumb != null) {
                            String raw = musicThumb.getRaw();
                            if (raw.contains("gif")) {
                                GlideUtil.loadGif(mContext, raw, musicThumbIv);
                            } else {
                                GlideUtil.load(mContext, raw, musicThumbIv);
                            }
                        }
                        musicSongNameTv.setText(meow.getSong_name() + "-" + meow.getArtist());
                        String music_duration = TimeUtil.msToString(meow.getMusic_duration() * 1000, TimeUtil.MIN_SEC);
                        musicDurationTv.setText(music_duration);
                        musicTitleTv.setText(meow.getTitle());
                        musicDescTv.setText(meow.getDescription());
                    }
                }
                break;
        }
    }

    public void setColors(ProgressBar progressBar, int backgroundColor, int progressColor) {
        //Background
        ClipDrawable bgClipDrawable = new ClipDrawable(new ColorDrawable(backgroundColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        bgClipDrawable.setLevel(10000);
        //Progress
        ClipDrawable progressClip = new ClipDrawable(new ColorDrawable(progressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        //Setup LayerDrawable and assign to progressBar
        Drawable[] progressDrawables = {bgClipDrawable, progressClip/*second*/, progressClip};
        LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);
        progressLayerDrawable.setId(0, android.R.id.background);
        progressLayerDrawable.setId(1, android.R.id.secondaryProgress);
        progressLayerDrawable.setId(2, android.R.id.progress);
        progressBar.setProgressDrawable(progressLayerDrawable);
    }

    public void setNineGridItemClickListener(ItemImageClickListener<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> itemImageViewClickListener) {
        mItemImageClickListener = itemImageViewClickListener;
    }
}
