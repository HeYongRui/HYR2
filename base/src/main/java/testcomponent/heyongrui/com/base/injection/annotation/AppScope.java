package testcomponent.heyongrui.com.base.injection.annotation;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by lambert on 2018/10/22.
 */

@Scope
@Retention(RUNTIME)
public @interface AppScope {
}
