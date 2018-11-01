package testcomponent.heyongrui.com.componenta.presenter;

import com.blankj.utilcode.util.LogUtils;

import javax.inject.Inject;

import testcomponent.heyongrui.com.componenta.contract.TestContract;

/**
 * Created by lambert on 2018/10/22.
 */

public class TestPresenter implements TestContract.Presenter {

    private TestContract.View mView;

    @Inject
    public TestPresenter() {

    }

    @Override
    public void attchView(TestContract.View view) {
        mView = view;
        LogUtils.i("绑定");
    }

    @Override
    public void detachView() {
        mView = null;
        LogUtils.i("解除");
    }

    @Override
    public void test() {
        mView.test();
        LogUtils.i("presenter test");
    }
}
