package testcomponent.heyongrui.com.componenta.ui.qingmang.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.billy.cc.core.component.CCUtil;

import java.util.ArrayList;
import java.util.List;

import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.base.BaseFragment;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;
import testcomponent.heyongrui.com.componenta.ui.qingmang.adapter.QingMangArticleFragmentPagerAdapter;

/**
 * Created by lambert on 2019/2/13.
 */

public class QingMangArticleActivity extends BaseActivity {

    TextView titleTv;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qingmang_article;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        addOnClickListeners(view -> {
            int id = view.getId();
            if (id == R.id.back_iv) {
                onBackPressed();
            }
        }, R.id.back_iv);
        titleTv = findViewById(R.id.title_tv);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
    }


    private void initData() {
        QingMangCategoriesDto.CategoriesBean categoriesBean = CCUtil.getNavigateParam(this, "categoriesBean", new QingMangCategoriesDto.CategoriesBean());
        titleTv.setText(categoriesBean.getName());
        List<QingMangCategoriesDto.CategoriesBean.SubCategoriesBean> subCategories = categoriesBean.getSubCategories();
        subCategories = subCategories == null ? new ArrayList<>() : subCategories;
        if (subCategories.isEmpty()) {
            QingMangCategoriesDto.CategoriesBean.SubCategoriesBean subCategoriesBean = new QingMangCategoriesDto.CategoriesBean.SubCategoriesBean();
            subCategoriesBean.setCategoryId(categoriesBean.getCategoryId());
            subCategoriesBean.setName(categoriesBean.getName());
            subCategories.add(subCategoriesBean);
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.removeAllTabs();
        List<BaseFragment> fragments = new ArrayList<>();
        List<String> pageTitles = new ArrayList<>();
        for (QingMangCategoriesDto.CategoriesBean.SubCategoriesBean subCategory : subCategories) {
            fragments.add(QingMangArticleFragment.newInstance(subCategory.getCategoryId()));
            pageTitles.add(subCategory.getName());
        }
        QingMangArticleFragmentPagerAdapter adapter = new QingMangArticleFragmentPagerAdapter(getSupportFragmentManager(), fragments, pageTitles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }
}
