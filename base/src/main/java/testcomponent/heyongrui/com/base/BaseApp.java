package testcomponent.heyongrui.com.base;

import android.app.Application;

import testcomponent.heyongrui.com.base.injection.component.BaseAppComponent;
import testcomponent.heyongrui.com.base.injection.component.DaggerBaseAppComponent;
import testcomponent.heyongrui.com.base.injection.module.BaseAppModule;

/**
 * Created by lambert on 2018/10/22.
 */

public abstract class BaseApp extends Application {
    protected static BaseApp baseApp;
    private BaseAppComponent baseAppComponent;

    public static BaseApp getInstance() {
        return baseApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        initializeInjector();
        initSDK();
    }

    protected void initializeInjector() {
        this.baseAppComponent = DaggerBaseAppComponent.builder()
                .baseAppModule(new BaseAppModule(this))
                .build();
        baseAppComponent.inject(this);
    }

    public BaseAppComponent getBaseAppComponent() {
        return this.baseAppComponent;
    }

    protected abstract void initSDK();
}