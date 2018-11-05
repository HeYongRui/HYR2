package testcomponent.heyongrui.com.componenta;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import testcomponent.heyongrui.com.componenta.ui.mono.view.MonoTeaActivity;
import testcomponent.heyongrui.com.componenta.ui.unsplash.view.UnsplashActivity;

/**
 * Created by lambert on 2018/9/28.
 */

public class ComponentA implements IComponent {

    private AtomicBoolean initialized = new AtomicBoolean(false);
    private final HashMap<String, BaseInterceptor> map = new HashMap<>();

    private void initProcessors() {
    }

    private void add(BaseInterceptor processor) {
        map.put(processor.getActionName(), processor);
    }

    @Override
    public String getName() {
        //组件的名称，调用此组件的方式：
        // CC.obtainBuilder("ComponentA")...build().callAsync()
        return "ComponentA";
    }

    /**
     * 组件被调用时的入口
     * 要确保每个逻辑分支都会调用到CC.sendCCResult，
     * 包括try-catch,if-else,switch-case-default,startActivity
     *
     * @param cc 组件调用对象，可从此对象中获取相关信息
     * @return true:将异步调用CC.sendCCResult(...),用于异步实现相关功能，例如：文件加载、网络请求等
     * false:会同步调用CC.sendCCResult(...),即在onCall方法return之前调用，否则将被视为不合法的实现
     */
    @Override
    public boolean onCall(CC cc) {
        if (initialized.compareAndSet(false, true)) {
            synchronized (map) {
                initProcessors();
            }
        }
        String actionName = cc.getActionName();
        if (TextUtils.equals("ComponentActivityA", actionName)) {
            Context context = cc.getContext();
            Map<String, Object> params = cc.getParams();
            Intent intent = new Intent(context, ComponentActivityA.class);
            if (params != null) {
                if (params instanceof Serializable) {
                    intent.putExtra("parms", (Serializable) params);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
            return false;
        } else if (TextUtils.equals("LoginInterceptor", actionName)) {
            BaseInterceptor baseInterceptor = map.get(actionName);
            if (baseInterceptor != null) {
                return baseInterceptor.onActionCall(cc);
            }
            CC.sendCCResult(cc.getCallId(), CCResult.error("has not support for action:" + cc.getActionName()));
        } else if (TextUtils.equals("openUnsplash", actionName)) {
            CCUtil.navigateTo(cc, UnsplashActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else if (TextUtils.equals("openMono", actionName)) {
            CCUtil.navigateTo(cc, MonoTeaActivity.class);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        }
        return false;
    }
}
