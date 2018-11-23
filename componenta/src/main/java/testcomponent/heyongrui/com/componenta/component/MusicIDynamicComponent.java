package testcomponent.heyongrui.com.componenta.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IDynamicComponent;
import com.billy.cc.core.component.IMainThread;

/**
 * Created by lambert on 2018/11/22.
 */

public class MusicIDynamicComponent implements IDynamicComponent, IMainThread {

    private final String musicStatusChangedObserver = "musicStatusChangedObserver";
    private MusicCallBack musicCallBack;

    public MusicIDynamicComponent() {
    }

    @Override
    public String getName() {
        return "MusicIDynamicComponent";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if (musicStatusChangedObserver.equals(actionName)) {
            //在进入此处时，当前线程一定为主线程（是在shouldActionRunOnMainThread方法中指定的）
            return onMusicStatusChanged(cc);
        }
        CC.sendCCResult(cc.getCallId(), CCResult.error("unsupported action:" + actionName));
        return false;
    }

    @Override
    public Boolean shouldActionRunOnMainThread(String actionName, CC cc) {
        //return true，onCall方法在主线程运行
        if (musicStatusChangedObserver.equals(actionName)) {
            return true;
        }
        return null;
    }

    private boolean onMusicStatusChanged(CC cc) {
        int currentPosition = cc.getParamItem("currentPosition", 0);
        int duration = cc.getParamItem("duration", 0);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        if (musicCallBack != null) {
            musicCallBack.onMusicStatusChanged(currentPosition, duration);
        }
        return false;
    }

    public interface MusicCallBack {
        void onMusicStatusChanged(int currentPosition, int duration);
    }

    public void setMusicCallBack(MusicCallBack musicCallBack) {
        this.musicCallBack = musicCallBack;
    }
}
