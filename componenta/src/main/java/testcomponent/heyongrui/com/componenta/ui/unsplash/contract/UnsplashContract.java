package testcomponent.heyongrui.com.componenta.ui.unsplash.contract;

import testcomponent.heyongrui.com.base.base.BasePresenter;
import testcomponent.heyongrui.com.base.base.BaseView;

/**
 * Created by lambert on 2018/10/24.
 */

public interface UnsplashContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter<View> {
    }
}
