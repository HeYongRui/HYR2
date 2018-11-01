package testcomponent.heyongrui.com.base.injection.component;



import dagger.Component;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.injection.annotation.PerActivity;
import testcomponent.heyongrui.com.base.injection.module.ActivityModule;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = BaseAppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(BaseActivity baseActivity);
}
