package testcomponent.heyongrui.com.componenta.ui.qingmang.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.billy.cc.core.component.CC;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;
import testcomponent.heyongrui.com.componenta.net.service.QingMangService;
import testcomponent.heyongrui.com.componenta.ui.qingmang.adapter.QingMangAdapter;
import testcomponent.heyongrui.com.componenta.ui.qingmang.adapter.QingMangMultipleItem;

/**
 * Created by lambert on 2019/2/12.
 */

public class QingMangCategoriesActivity extends BaseActivity {

    @Inject
    QingMangService mQingMangService;

    private QingMangAdapter mQingMangCategoriesAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qingmang_categories;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initInject();
        RecyclerView categoriesRcl = findViewById(R.id.categories_rcl);
        initRecyclerView(categoriesRcl);
        getQingMangCategories();
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(this))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }

    private void initRecyclerView(RecyclerView categoriesRcl) {
        mQingMangCategoriesAdapter = new QingMangAdapter(new ArrayList<>());
        mQingMangCategoriesAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        categoriesRcl.setLayoutManager(manager);
        mQingMangCategoriesAdapter.bindToRecyclerView(categoriesRcl);
        mQingMangCategoriesAdapter.setOnItemClickListener((adapter, view, position) -> {
            QingMangCategoriesDto.CategoriesBean categoriesBean = mQingMangCategoriesAdapter.getData().get(position).getCategoriesBean();
            if (categoriesBean == null) return;
            CC.obtainBuilder("ComponentA")
                    .addParam("categoriesBean", categoriesBean)
                    .setActionName("openQingMangArticle")
                    .build()
                    .callAsync();
        });
    }

    private void getQingMangCategories() {
        mRxManager.add(mQingMangService.getQingMangCategories()
                .subscribeWith(new ResponseDisposable<QingMangCategoriesDto>(this, true) {
                    @Override
                    protected void onSuccess(QingMangCategoriesDto qingMangCategoriesDto) {
                        parseCategoriesData(qingMangCategoriesDto);
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }

    private void parseCategoriesData(QingMangCategoriesDto qingMangCategoriesDto) {
        mQingMangCategoriesAdapter.setNewData(null);
        if (qingMangCategoriesDto == null) return;
        List<QingMangCategoriesDto.CategoriesBean> categories = qingMangCategoriesDto.getCategories();
        if (categories == null || categories.isEmpty()) return;
        for (QingMangCategoriesDto.CategoriesBean category : categories) {
            mQingMangCategoriesAdapter.addData(new QingMangMultipleItem(QingMangMultipleItem.QINGMANG_CATEGORY, 1, category));
        }
    }
}
