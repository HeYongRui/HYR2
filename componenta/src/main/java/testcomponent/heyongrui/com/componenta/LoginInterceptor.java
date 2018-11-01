package testcomponent.heyongrui.com.componenta;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;

import testcomponent.heyongrui.com.base.Global;

/**
 * Created by lambert on 2018/9/30.
 */

public class LoginInterceptor implements BaseInterceptor {
    @Override
    public String getActionName() {
        return "LoginInterceptor";
    }

    @Override
    public boolean onActionCall(CC cc) {
        if (Global.loginUser != null) {
            //already login, return username
            CCResult result = CCResult.success(Global.KEY_USER, Global.loginUser);
            CC.sendCCResult(cc.getCallId(), result);
            return false;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CC.getApplication(), "please login first!", Toast.LENGTH_SHORT).show();
            }
        });
        CCUtil.navigateTo(cc, LoginActivity.class);
        //不立即调用CC.sendCCResult,返回true
        return true;
    }
}
