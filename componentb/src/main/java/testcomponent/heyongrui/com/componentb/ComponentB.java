package testcomponent.heyongrui.com.componentb;

import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;
import com.billy.cc.core.component.IComponentCallback;
import com.billy.cc.core.component.IMainThread;
import com.blankj.utilcode.util.ToastUtils;

import testcomponent.heyongrui.com.base.Global;

/**
 * Created by lambert on 2018/9/28.
 */

public class ComponentB implements IComponent, IMainThread {

    @Override
    public String getName() {
        //组件的名称，调用此组件的方式
        return "ComponentB";
    }

    @Override
    public boolean onCall(final CC cc) {
        String actionName = cc.getActionName();
        if (TextUtils.equals("addLoginObserver", actionName)) {
            //添加动态组件
            String dynamicComponentName = cc.getParamItem("componentName");
            String dynamicActionName = cc.getParamItem("actionName");
            boolean success = IDynamicComponentManager.addObserver(dynamicComponentName, dynamicActionName);
            CCResult result = success ? CCResult.success() : CCResult.error("");
            CC.sendCCResult(cc.getCallId(), result);
            return false;
        } else if (TextUtils.equals("removeLoginObserver", actionName)) {
            //移除动态组件
            String dynamicComponentName = cc.getParamItem("componentName");
            IDynamicComponentManager.removeObserver(dynamicComponentName);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
            return false;
        } else if (TextUtils.equals("setUserInfo", actionName)) {
            Global.User user = cc.getParamItem(Global.KEY_USER);
            IDynamicComponentManager.setLoginUser(user);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
            return false;
        } else {
            CC.obtainBuilder("UserComponent")
                    .setActionName("checkLogin")
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc1, CCResult result) {
                            ToastUtils.showShort(result.toString() + "");
                            if (result.isSuccess()) {
                                //登录成功，打开目标页面
                                CCUtil.navigateTo(cc1, ComponentActivityB.class);
                            }
                            CC.sendCCResult(cc.getCallId(), result);
                        }
                    });
            return true;
        }
    }

    @Override
    public Boolean shouldActionRunOnMainThread(String actionName, CC cc) {
        return null;
    }
}
