package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.base.util.DrawableUtil;
import testcomponent.heyongrui.com.base.widget.itemdecoration.RecycleViewItemDecoration;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.net.dto.MonoCategoryDto;
import testcomponent.heyongrui.com.componenta.net.service.MonoSerevice;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoAdapter;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoMultipleItem;

/**
 * Created by lambert on 2018/11/21.
 */

public class MonoCategoryActivity extends BaseActivity {
    private TextView titleTv;

    private MonoAdapter monoAdapter;
    private int mCategoryId;
    private Integer mStart;
    private boolean mIsLastPage;

    @Inject
    MonoSerevice monoSerevice;

    public static void launchActivity(Context context, int category_id, String category_name) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("category_id", category_id);
        bundle.putString("category_name", category_name);
        intent.putExtras(bundle);
        intent.setClass(context, MonoCategoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mono_category;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initInject();
        initView();
        initData();
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(this))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }


    private void initView() {
        ImageView backIv = findViewById(R.id.back_iv);
        titleTv = findViewById(R.id.title_tv);
        Drawable drawable = DrawableUtil.tintDrawable(this, R.drawable.back, R.color.white);
        backIv.setImageDrawable(drawable);
        addOnClickListeners(view -> {
            int id = view.getId();
            if (id == R.id.back_iv) {
                finish();
            }
        }, R.id.back_iv);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        StoreHouseHeader storeHouseHeader = findViewById(R.id.storeHouseHeader);
        initSwipeRefresh(refreshLayout, storeHouseHeader);
    }

    private void initSwipeRefresh(SmartRefreshLayout smartRefreshLayout, StoreHouseHeader storeHouseHeader) {
        storeHouseHeader.setTextColor(ContextCompat.getColor(this, R.color.white));
        storeHouseHeader.setPrimaryColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        smartRefreshLayout.setEnableNestedScroll(true);
        smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
                if (mIsLastPage) {
                    refreshlayout.setLoadmoreFinished(true);
                } else {
                    getCategory(false);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
                refreshlayout.setLoadmoreFinished(false);
                getCategory(true);
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        List<MonoMultipleItem> data = new ArrayList<>();
        monoAdapter = new MonoAdapter(data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        monoAdapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewItemDecoration(this, 1));
        monoAdapter.setOnItemClickListener((adapter, view, position) -> {
            MonoCategoryDto.MeowsBean meowsBean = monoAdapter.getData().get(position).getMeowsBean();
            if (meowsBean == null) return;
            MonoCategoryDto.MeowBean meow = meowsBean.getMeow();
            if (meow == null) return;
            MonoCategoryDetailActivity.launchActivity(this, meow.getId());
        });
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        mCategoryId = extras.getInt("category_id", -1);
        String category_name = extras.getString("category_name");
        titleTv.setText(category_name);
        if (mCategoryId == -1) return;
        getCategory(true);
    }

    private void getCategory(boolean is_clear_data) {
        mStart = is_clear_data ? 0 : mStart + 1;
        mRxManager.add(monoSerevice.getCategory(mCategoryId, mStart)
                .subscribeWith(new ResponseDisposable<MonoCategoryDto>(this) {
                    @Override
                    protected void onSuccess(MonoCategoryDto monoCategoryDto) {
                        if (monoCategoryDto == null) return;
                        mIsLastPage = monoCategoryDto.isIs_last_page();
                        List<MonoCategoryDto.MeowsBean> meows = monoCategoryDto.getMeows();
                        if (meows == null || meows.isEmpty()) return;
                        if (is_clear_data) {
                            monoAdapter.getData().clear();
                        }
                        for (MonoCategoryDto.MeowsBean meowsBean : meows) {
                            monoAdapter.addData(new MonoMultipleItem(MonoMultipleItem.TYPE_CATEGORY, meowsBean));
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }
}
