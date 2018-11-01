package testcomponent.heyongrui.com.componenta;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

/**
 * Created by lambert on 2018/9/30.
 */

public interface BaseInterceptor {
    String getActionName();

    /**
     * action的处理类
     *
     * @param cc cc
     * @return 是否异步调用 {@link CC#sendCCResult(String, CCResult)} . true：异步， false：同步调用
     */
    boolean onActionCall(CC cc);
}
