package testcomponent.heyongrui.com.interceptor;

import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.Chain;
import com.billy.cc.core.component.ICCInterceptor;
import com.orhanobut.logger.Logger;

/**
 * Created by lambert on 2018/10/8.
 * 自定义调用局部拦截器
 */

public class CustomICCInterceptor implements ICCInterceptor {

    @Override
    public CCResult intercept(Chain chain) {
        Logger.i("============log before:" + chain.getCC());
        CCResult result = chain.proceed();
        Logger.i("============log after:" + result);
        return result;
    }
}
