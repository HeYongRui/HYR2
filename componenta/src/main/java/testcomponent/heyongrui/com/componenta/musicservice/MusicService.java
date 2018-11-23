package testcomponent.heyongrui.com.componenta.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.billy.cc.core.component.CC;

/**
 * Created by lambert on 2018/11/22.
 */

public class MusicService extends Service {

    private MediaPlayer mMediaPlayer;
    private boolean mIsStopService;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.setLooping(false);
        }
        mIsStopService = false;
        updateMusicProgress();
        return new MusicBinder(mMediaPlayer);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsStopService = true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.release();
        }
        return super.onUnbind(intent);
    }

    private void updateMusicProgress() {
        //开启线程发送数据更新UI
        new Thread() {
            @Override
            public void run() {
                while (!mIsStopService) {
                    try {
                        if (mMediaPlayer == null) break;
                        if (mMediaPlayer.isPlaying()) {
                            int currentPosition = mMediaPlayer.getCurrentPosition();
                            int duration = mMediaPlayer.getDuration();
                            CC.obtainBuilder("MusicIDynamicComponent")
                                    .setActionName("musicStatusChangedObserver")
                                    .addParam("currentPosition", currentPosition)
                                    .addParam("duration", duration)
                                    .build()
                                    .callAsync();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}