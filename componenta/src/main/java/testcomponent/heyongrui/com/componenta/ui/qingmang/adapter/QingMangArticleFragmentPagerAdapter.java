package testcomponent.heyongrui.com.componenta.ui.qingmang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import testcomponent.heyongrui.com.base.base.BaseFragment;

/**
 * Created by lambert on 2019/2/14.
 */

public class QingMangArticleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;
    private List<String> pageTitles;

    public QingMangArticleFragmentPagerAdapter(FragmentManager manager, List<BaseFragment> fragments, List<String> pageTitles) {
        super(manager);
        this.fragments = fragments;
        this.pageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (pageTitles == null || pageTitles.isEmpty()) return "";
        return pageTitles.get(position);
    }
}
