package testcomponent.heyongrui.com.componenta;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IDynamicComponent;
import com.billy.cc.core.component.IMainThread;
import com.blankj.utilcode.util.ToastUtils;

import testcomponent.heyongrui.com.base.Global;

/**
 * Created by lambert on 2018/10/10.
 * 动态组件
 */

public class CustomIDynamicComponent implements IDynamicComponent, IMainThread {
    @NonNull
    String observerComponentName;
    static final String OBSERVER_ACTION_NAME = "loginUserState";

    private OnLoginUserChangedCallBack onLoginUserChangedCallBack;

    CustomIDynamicComponent() {
        //指定此动态组件的ComponentName为一个唯一值，不会因为activity有多个对象而出现重复
        this.observerComponentName = "mainActivityUserObserver_" + SystemClock.uptimeMillis();
    }

    @Override
    public String getName() {
        return observerComponentName;
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if (OBSERVER_ACTION_NAME.equals(actionName)) {
            //在进入此处时，当前线程一定为主线程（是在shouldActionRunOnMainThread方法中指定的）
            return onLoginUserChanged(cc);
        }
        CC.sendCCResult(cc.getCallId(), CCResult.error("unsupported action:" + actionName));
        return false;
    }

    private boolean onLoginUserChanged(CC cc) {
        Global.User user = cc.getParamItem("user");
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        if (onLoginUserChangedCallBack != null) {
            onLoginUserChangedCallBack.onLoginUserChanged(user);
        }
        ToastUtils.showShort("测试动态组件");
        return false;
    }

    @Override
    public Boolean shouldActionRunOnMainThread(String actionName, CC cc) {
        //return true，onCall方法在主线程运行
        if (OBSERVER_ACTION_NAME.equals(actionName)) {
            return true;
        }
        return null;
    }

    public interface OnLoginUserChangedCallBack {
        void onLoginUserChanged(Global.User user);
    }

    public void setOnLoginUserChangedCallBack(OnLoginUserChangedCallBack onLoginUserChangedCallBack) {
        this.onLoginUserChangedCallBack = onLoginUserChangedCallBack;
    }
}
