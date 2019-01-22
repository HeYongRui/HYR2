package testcomponent.heyongrui.com.componenta.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lambert on 2018/11/22.
 */

public class MusicService extends Service {

    private MediaPlayer mMediaPlayer;
    private boolean mIsStopService;
    private MusicBinder musicBinder;

    private int playStatus;//播放状态 0-未开始 1-播放中 2-暂停 3-停止
    private int playItemPosition;//歌曲所在播放列表的位置

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
        if (musicBinder == null) {
            musicBinder = new MusicBinder(mMediaPlayer, this);
            mMediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
            });
            mMediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                setPlayStatus(0);
                int duration = mediaPlayer1.getDuration();
                sendMusicBroadcast(duration, duration);
            });
        }
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsStopService = true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.release();
        }
        return super.onUnbind(intent);
    }

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }

    public int getPlayItemPosition() {
        return playItemPosition;
    }

    public void setPlayItemPosition(int playItemPosition) {
        this.playItemPosition = playItemPosition;
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
                            sendMusicBroadcast(currentPosition, duration);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void sendMusicBroadcast(int currentPosition, int duration) {
        Intent intent = new Intent();
        intent.setAction("testcomponent.heyongrui.com.componenta.musicservice");
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("duration", duration);
        intent.putExtra("playItemPosition", playItemPosition);
        sendBroadcast(intent);
    }
}