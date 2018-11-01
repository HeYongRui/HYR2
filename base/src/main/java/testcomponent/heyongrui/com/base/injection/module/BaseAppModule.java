package testcomponent.heyongrui.com.base.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.injection.annotation.ApplicationContext;

/**
 * Provide application-level dependencies. Mainly singleton object that can be injected from
 * anywhere in the baseApp.
 */
@Module
public class BaseAppModule {
    protected final BaseApp mBaseApp;

    public BaseAppModule(BaseApp baseApp) {
        mBaseApp = baseApp;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mBaseApp;
    }

    @Provides
    @Singleton
    BaseApp provideBaseApp() {
        return mBaseApp;
    }
}