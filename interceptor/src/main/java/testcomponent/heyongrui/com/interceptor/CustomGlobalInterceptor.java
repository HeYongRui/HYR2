package testcomponent.heyongrui.com.interceptor;

import android.util.Log;

import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.Chain;
import com.billy.cc.core.component.IGlobalCCInterceptor;

/**
 * Created by lambert on 2018/10/8.
 * 自定义全局拦截器
 */

public class CustomGlobalInterceptor implements IGlobalCCInterceptor {
    private static final String TAG = "CustomGlobalInterceptor";

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public CCResult intercept(Chain chain) {
        Log.i(TAG, "============log before:" + chain.getCC());
        CCResult result = chain.proceed();
        Log.i(TAG, "============log after:" + result.toString());
        return result;
    }
}
