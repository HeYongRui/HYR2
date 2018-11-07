package debug;

import android.app.Application;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @author billy.qi
 * @since 17/11/20 20:02
 * 组件单独作为APP时使用，作为组件不会执行
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.enableRemoteCC(true);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("X5", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
