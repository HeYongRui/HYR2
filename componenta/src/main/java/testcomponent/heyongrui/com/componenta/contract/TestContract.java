package testcomponent.heyongrui.com.componenta.contract;

import testcomponent.heyongrui.com.base.base.BasePresenter;
import testcomponent.heyongrui.com.base.base.BaseView;

/**
 * Created by lambert on 2018/10/22.
 */

public interface TestContract {
    interface View extends BaseView<Presenter> {
        void test();
    }

    interface Presenter extends BasePresenter<View> {
        void test();
    }
}
