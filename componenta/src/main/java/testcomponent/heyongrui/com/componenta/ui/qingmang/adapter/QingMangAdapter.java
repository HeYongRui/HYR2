package testcomponent.heyongrui.com.componenta.ui.qingmang.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.base.util.UiUtil;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangArticleListDto;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class QingMangAdapter extends BaseMultiItemQuickAdapter<QingMangMultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public QingMangAdapter(List<QingMangMultipleItem> data) {
        super(data);
        addItemType(QingMangMultipleItem.QINGMANG_CATEGORY, R.layout.adapter_item_qingmang_category);
        addItemType(QingMangMultipleItem.QINGMANG_ARTICLE_TEXT, R.layout.adapter_item_qingmang_article_text);
        addItemType(QingMangMultipleItem.QINGMANG_ARTICLE_IMAGE, R.layout.adapter_item_qingmang_article_img);
    }

    @Override
    protected void convert(BaseViewHolder helper, QingMangMultipleItem item) {
        UiUtil.setOnclickFeedBack(mContext, R.color.white, R.color.gray, helper.itemView);
        switch (helper.getItemViewType()) {
            case QingMangMultipleItem.QINGMANG_CATEGORY: {
                ImageView iconIv = helper.getView(R.id.icon_iv);
                TextView nameTv = helper.getView(R.id.name_tv);
                QingMangCategoriesDto.CategoriesBean categoriesBean = item.getCategoriesBean();
                if (categoriesBean == null) return;
                GlideUtil.load(mContext, categoriesBean.getIcon(), iconIv);
                nameTv.setText(categoriesBean.getName());
            }
            break;
            case QingMangMultipleItem.QINGMANG_ARTICLE_TEXT: {
                ImageView iconIv = helper.getView(R.id.icon_iv);
                TextView categoryTv = helper.getView(R.id.category_tv);
                TextView timeTv = helper.getView(R.id.time_tv);
                ImageView coverIv = helper.getView(R.id.cover_iv);
                TextView titleTv = helper.getView(R.id.title_tv);
                TextView descTv = helper.getView(R.id.desc_tv);
                QingMangArticleListDto.ArticlesBean articlesBean = item.getArticlesBean();
                if (articlesBean == null) return;
                String providerIcon = articlesBean.getProviderIcon();
                if (providerIcon.contains("gif")) {
                    GlideUtil.loadGif(mContext, providerIcon, iconIv);
                } else {
                    GlideUtil.load(mContext, providerIcon, iconIv);
                }
                categoryTv.setText(articlesBean.getProviderName());
                timeTv.setText(TimeUtil.getDistanceTime(articlesBean.getPublishTimestamp()));
                List<QingMangArticleListDto.ArticlesBean.CoverBean> covers = articlesBean.getCovers();
                if (covers != null && !covers.isEmpty()) {
                    coverIv.setVisibility(View.VISIBLE);
                    QingMangArticleListDto.ArticlesBean.CoverBean coverBean = covers.get(0);
                    String url = coverBean.getUrl();
                    if (url.contains("gif")) {
                        GlideUtil.loadGif(mContext, url, coverIv);
                    } else {
                        GlideUtil.load(mContext, url, coverIv);
                    }
                } else {
                    coverIv.setVisibility(View.GONE);
                }
                titleTv.setText(articlesBean.getTitle());
                descTv.setText(articlesBean.getSnippet());
            }
            break;
            case QingMangMultipleItem.QINGMANG_ARTICLE_IMAGE: {
                ImageView iconIv = helper.getView(R.id.icon_iv);
                TextView categoryTv = helper.getView(R.id.category_tv);
                TextView timeTv = helper.getView(R.id.time_tv);
                ImageView coverIv = helper.getView(R.id.cover_iv);
                TextView titleTv = helper.getView(R.id.title_tv);
                TextView descTv = helper.getView(R.id.desc_tv);
                QingMangArticleListDto.ArticlesBean articlesBean = item.getArticlesBean();
                if (articlesBean == null) return;
                String providerIcon = articlesBean.getProviderIcon();
                if (providerIcon.contains("gif")) {
                    GlideUtil.loadGif(mContext, providerIcon, iconIv);
                } else {
                    GlideUtil.load(mContext, providerIcon, iconIv);
                }
                categoryTv.setText(articlesBean.getProviderName());
                timeTv.setText(TimeUtil.getDistanceTime(articlesBean.getPublishTimestamp()));
                List<QingMangArticleListDto.ArticlesBean.CoverBean> imagesList = new ArrayList<>();
                List<QingMangArticleListDto.ArticlesBean.CoverBean> covers = articlesBean.getCovers();
                List<QingMangArticleListDto.ArticlesBean.CoverBean> images = articlesBean.getImages();
                if (covers != null && !covers.isEmpty()) {
                    imagesList.addAll(covers);
                }
                if (images != null && !images.isEmpty()) {
                    imagesList.addAll(images);
                }
                if (!imagesList.isEmpty()) {
                    coverIv.setVisibility(View.VISIBLE);
                    QingMangArticleListDto.ArticlesBean.CoverBean coverBean = imagesList.get(0);
                    //根据返回的图片尺寸动态设置图片控件高度
                    ConstraintLayout itemView = (ConstraintLayout) helper.itemView;
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(itemView);
                    constraintSet.setDimensionRatio(coverIv.getId(), ((float) coverBean.getWidth() / (float) coverBean.getHeight()) + "");
                    constraintSet.applyTo(itemView);
                    String url = coverBean.getUrl();
                    if (url.contains("gif")) {
                        GlideUtil.loadGif(mContext, url, coverIv);
                    } else {
                        GlideUtil.load(mContext, url, coverIv);
                    }
                } else {
                    coverIv.setVisibility(View.GONE);
                }
                titleTv.setText(articlesBean.getTitle());
                descTv.setText(articlesBean.getSnippet());
            }
            break;
        }
    }
}
