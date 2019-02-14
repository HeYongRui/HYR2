package testcomponent.heyongrui.com.componenta.ui.qingmang.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.base.BaseFragment;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.base.widget.itemdecoration.RecycleViewItemDecoration;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangArticleListDto;
import testcomponent.heyongrui.com.componenta.net.service.QingMangService;
import testcomponent.heyongrui.com.componenta.ui.H5Activity;
import testcomponent.heyongrui.com.componenta.ui.qingmang.adapter.QingMangAdapter;
import testcomponent.heyongrui.com.componenta.ui.qingmang.adapter.QingMangMultipleItem;

/**
 * Created by lambert on 2019/2/14.
 */

public class QingMangArticleFragment extends BaseFragment {

    SmartRefreshLayout refreshLayout;

    @Inject
    QingMangService mQingMangService;

    private QingMangAdapter mQingMangArticleAdapter;
    private String mCategoryId;
    private boolean mHasMore;
    private String mNextUrl;

    public static QingMangArticleFragment newInstance(String category_id) {
        QingMangArticleFragment fragment = new QingMangArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category_id", category_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qingmang_article;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initInject();
        initData();
        initView();
    }

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(getActivity()))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        mCategoryId = arguments.getString("category_id");
    }

    private void initView() {
        //初始化刷新控件
        refreshLayout = mView.findViewById(R.id.refreshLayout);
        StoreHouseHeader storeHouseHeader = mView.findViewById(R.id.storeHouseHeader);
        storeHouseHeader.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        storeHouseHeader.setPrimaryColors(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        refreshLayout.setEnableNestedScroll(true);
        refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mHasMore) {
                    getQingMangArticleList(null, mNextUrl, false);
                } else {
                    refreshlayout.setLoadmoreFinished(true);
                    refreshLayout.finishLoadmore();
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.setLoadmoreFinished(false);
                getQingMangArticleList(mCategoryId, null, true);
            }
        });
        //初始化列表
        RecyclerView articleRcl = mView.findViewById(R.id.article_rcl);
        initRecyclerView(articleRcl);
    }

    private void initRecyclerView(RecyclerView articleRcl) {
        mQingMangArticleAdapter = new QingMangAdapter(new ArrayList<>());
        mQingMangArticleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleRcl.setLayoutManager(new LinearLayoutManager(mContext));
        articleRcl.addItemDecoration(new RecycleViewItemDecoration(mContext, 10));
        mQingMangArticleAdapter.bindToRecyclerView(articleRcl);
        mQingMangArticleAdapter.setOnItemClickListener((adapter, view, position) -> {
            QingMangArticleListDto.ArticlesBean articlesBean = mQingMangArticleAdapter.getData().get(position).getArticlesBean();
            String webUrl = articlesBean.getWebUrl();
            H5Activity.launchActivity(mContext, webUrl);
        });
    }

    private void getQingMangArticleList(String category_id, String next_url, boolean isResetData) {
        mRxManager.add(mQingMangService.getQingMangArticleList(category_id, next_url)
                .subscribeWith(new ResponseDisposable<QingMangArticleListDto>(mContext) {
                    @Override
                    protected void onSuccess(QingMangArticleListDto qingMangArticleListDto) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadmore();
                        refreshLayout.setLoadmoreFinished(false);
                        if (isResetData) {
                            mQingMangArticleAdapter.setNewData(null);
                        }
                        dealData(qingMangArticleListDto);
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }

    private void dealData(QingMangArticleListDto qingMangArticleListDto) {
        if (qingMangArticleListDto == null) return;
        mHasMore = qingMangArticleListDto.isHasMore();
        mNextUrl = qingMangArticleListDto.getNextUrl();
        qingMangArticleListDto.getNextUrl();
        List<QingMangArticleListDto.ArticlesBean> articles = qingMangArticleListDto.getArticles();
        if (articles == null || articles.isEmpty()) return;
        for (QingMangArticleListDto.ArticlesBean article : articles) {
            List<QingMangArticleListDto.ArticlesBean.CoverBean> covers = article.getCovers();
            if (covers != null && !covers.isEmpty()) {
                QingMangArticleListDto.ArticlesBean.CoverBean coverBean = covers.get(0);
                int width = coverBean.getWidth();
                if (width > 500) {
                    mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_IMAGE, 1, article));
                } else {
                    mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_TEXT, 1, article));
                }
            } else {
                mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_TEXT, 1, article));
            }
//            String templateType = article.getTemplateType();
//            switch (templateType) {
//                case "text"://一般的长文，默认样式
//                    mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_TEXT, 1, article));
//                    break;
//                case "short_text"://短文本
//                    break;
//                case "video"://视频
//                    break;
//                case "image"://单图
//                    mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_IMAGE, 1, article));
//                    break;
//                case "gallery"://多图
//                    mQingMangArticleAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_ARTICLE_IMAGE, 1, article));
//                    break;
//            }
        }
    }
}
