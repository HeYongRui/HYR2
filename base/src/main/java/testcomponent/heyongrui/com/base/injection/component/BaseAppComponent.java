package testcomponent.heyongrui.com.base.injection.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.injection.annotation.ApplicationContext;
import testcomponent.heyongrui.com.base.injection.module.BaseAppModule;

@Singleton
@Component(modules = BaseAppModule.class)
public interface BaseAppComponent {

    void inject(BaseApp baseApp);

    BaseApp application();

    @ApplicationContext
    Context context();
}