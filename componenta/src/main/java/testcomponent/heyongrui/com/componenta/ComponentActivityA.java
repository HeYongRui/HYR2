package testcomponent.heyongrui.com.componenta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.blankj.utilcode.util.LogUtils;

import java.io.Serializable;
import java.util.HashMap;

import javax.inject.Inject;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.Global;
import testcomponent.heyongrui.com.base.JsonFormat;
import testcomponent.heyongrui.com.base.base.RxManager;
import testcomponent.heyongrui.com.componenta.contract.TestContract;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.presenter.TestPresenter;

/**
 * Created by lambert on 2018/9/28.
 */

public class ComponentActivityA extends AppCompatActivity implements TestContract.View {

    private final static String TAG = "ComponentActivityA";

    private CustomIDynamicComponent customIDynamicComponent;
    private RxManager rxManager;
    private TextView textView;

    @Inject
    TestInjectDto testInjectDto;

    @Inject
    TestPresenter mPreseter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        rxManager = new RxManager();
        mPreseter.attchView(this);
        testInjectDto.setTest("test inject success");
        LogUtils.i(testInjectDto.getTest());
        textView = new TextView(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreseter.test();
                CC.obtainBuilder("ComponentB")
                        .build()
                        .callAsync();
            }
        });
        setContentView(textView);
        textView.setGravity(Gravity.CENTER);
        textView.setText("ComponentActivityA");
        addDynamicComponent();

        Serializable parms = getIntent().getSerializableExtra("parms");
        StringBuffer stringBuffer = new StringBuffer();
        if (parms != null) {
            if (parms instanceof HashMap) {
                Object bundle = ((HashMap) parms).get("bundle");
                if (bundle instanceof Bundle) {
                    String test = ((Bundle) bundle).getString("result");
                    test = "\n" + JsonFormat.format(test);
                    stringBuffer.append(test);
                    textView.setText(test);
                    Log.i(TAG, "onCreate: " + test);
                }
                Object key = ((HashMap) parms).get("loginCC");
                if (key instanceof String) {
                    String loginCC = "\n" + JsonFormat.format((String) key);
                    stringBuffer.append(loginCC);
                    Log.i(TAG, "onCreate: " + loginCC);
                }
            }
        }
        if (!TextUtils.isEmpty(stringBuffer.toString())) {
            textView.setText(stringBuffer);
        }
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(this))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }

    private void addDynamicComponent() {
        if (customIDynamicComponent == null) {
            //创建动态组件对象
            customIDynamicComponent = new CustomIDynamicComponent();
            customIDynamicComponent.setOnLoginUserChangedCallBack(new CustomIDynamicComponent.OnLoginUserChangedCallBack() {
                @Override
                public void onLoginUserChanged(Global.User user) {
                    if (user != null) {
                        textView.setText(user.getUserName());
                    }
                }
            });
            //向CC注册此动态组件
            // 登录状态改变后，UserStateManager.onUserLoginStateUpdated()方法中会通过CC调用通知此组件当前的登录状态
            CC.registerComponent(customIDynamicComponent);
            //通过CC调用ComponentB，将此动态组件注册为用户登录状态的监听器
            CC.obtainBuilder("ComponentB")
                    .setActionName("addLoginObserver")
                    .addParam("componentName", customIDynamicComponent.getName())
                    .addParam("actionName", CustomIDynamicComponent.OBSERVER_ACTION_NAME)
                    .build()
                    .callAsync();
//            loginUserButton.setText(R.string.unobserve_login_user);
        }
    }

    private void removeDynamicComponent() {
        if (customIDynamicComponent != null) {
            //从CC框架中注销此动态组件
            CC.unregisterComponent(customIDynamicComponent);
            //从ComponentB的登录状态监听列表中移除此动态组件：此后，登录状态改变将不再尝试通知此动态组件
            CC.obtainBuilder("ComponentB")
                    .setActionName("removeLoginObserver")
                    .addParam("componentName", customIDynamicComponent.getName())
                    .build()
                    .callAsync();
            customIDynamicComponent = null;
//            loginUserButton.setText(R.string.observe_login_user);
//            loginUserTextView.setText("");
        }
    }

    @Override
    public void test() {
        LogUtils.i("view test");
    }

    @Override
    protected void onDestroy() {
        removeDynamicComponent();
        rxManager.clear();
        mPreseter.detachView();
        super.onDestroy();
    }
}
