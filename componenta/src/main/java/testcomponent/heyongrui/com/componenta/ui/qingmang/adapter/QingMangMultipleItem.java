package testcomponent.heyongrui.com.componenta.ui.qingmang.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import testcomponent.heyongrui.com.componenta.net.dto.QingMangArticleListDto;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class QingMangMultipleItem implements MultiItemEntity {

    public static final int QINGMANG_CATEGORY = 10001;
    public static final int QINGMANG_ARTICLE_TEXT = 10002;
    public static final int QINGMANG_ARTICLE_SHORT_TEXT = 10003;
    public static final int QINGMANG_ARTICLE_VIDEO = 10004;
    public static final int QINGMANG_ARTICLE_IMAGE = 10005;
    public static final int QINGMANG_ARTICLE_GALLERY = 10006;

    private int itemType;
    private int spanSize;
    private QingMangCategoriesDto.CategoriesBean categoriesBean;
    private QingMangArticleListDto.ArticlesBean articlesBean;

    public QingMangMultipleItem(int itemType, int spanSize, QingMangCategoriesDto.CategoriesBean categoriesBean) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.categoriesBean = categoriesBean;
    }

    public QingMangMultipleItem(int itemType, int spanSize, QingMangArticleListDto.ArticlesBean articlesBean) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.articlesBean = articlesBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public QingMangCategoriesDto.CategoriesBean getCategoriesBean() {
        return categoriesBean;
    }

    public void setCategoriesBean(QingMangCategoriesDto.CategoriesBean categoriesBean) {
        this.categoriesBean = categoriesBean;
    }

    public QingMangArticleListDto.ArticlesBean getArticlesBean() {
        return articlesBean;
    }

    public void setArticlesBean(QingMangArticleListDto.ArticlesBean articlesBean) {
        this.articlesBean = articlesBean;
    }
}

