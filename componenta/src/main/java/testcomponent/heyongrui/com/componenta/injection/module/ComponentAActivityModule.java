package testcomponent.heyongrui.com.componenta.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import testcomponent.heyongrui.com.base.injection.annotation.PerActivity;
import testcomponent.heyongrui.com.componenta.TestInjectDto;

@Module
public class ComponentAActivityModule {

    private Activity mActivity;

    public ComponentAActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    Context providesContext() {
        return mActivity;
    }

    @PerActivity
    @Provides
    TestInjectDto provideTestInFInjectDto() {
        return new TestInjectDto();
    }
}