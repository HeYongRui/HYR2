package com.heyongrui.testcomponent;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.util.CoreUtil;

/**
 * Created by lambert on 2018/10/22.
 */

public class AppContext extends BaseApp {
    @Override
    protected void initSDK() {
        CoreUtil.init(this);
        initializeInjector();
    }
}
