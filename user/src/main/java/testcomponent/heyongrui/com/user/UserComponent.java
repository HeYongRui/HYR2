package testcomponent.heyongrui.com.user;

import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;

import testcomponent.heyongrui.com.base.Global;

/**
 * Created by lambert on 2018/10/8.
 */

public class UserComponent implements IComponent {
    @Override
    public String getName() {
        return "UserComponent";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if (TextUtils.equals("checkLogin", actionName)) {
            Global.User loginUser = Global.loginUser;
            if (loginUser != null) {//已经登录
                String userName = loginUser.getUserName();
                if (TextUtils.equals("Mr.He", userName)) {
                    CCResult ccResult = CCResult.success(Global.KEY_USER, loginUser);
                    CC.sendCCResult(cc.getCallId(), ccResult);
                    return false;
                }
            }
            //没有登录
            CCUtil.navigateTo(cc, LoginActivity.class);
            return true;
        }
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
