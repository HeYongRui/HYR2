package testcomponent.heyongrui.com.componenta.musicservice;

import android.media.MediaPlayer;
import android.os.Binder;

import com.billy.cc.core.component.CC;

import java.io.IOException;

/**
 * Created by lambert on 2018/11/22.
 */

public class MusicBinder extends Binder implements MusicOperation {

    private MediaPlayer mediaPlayer;
    private int playStatus;//播放状态 0-未开始 1-播放中 2-暂停 3-停止

    public MusicBinder(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();
        });
        this.mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
            int duration = mediaPlayer1.getDuration();
            CC.obtainBuilder("MusicIDynamicComponent")
                    .setActionName("musicStatusChangedObserver")
                    .addParam("currentPosition", duration)
                    .addParam("duration", duration)
                    .build()
                    .callAsync();
        });
    }

    @Override
    public void startPlay(String url) {//播放新的歌曲
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();//网络视频，异步
            playStatus = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {//播放
        if (mediaPlayer == null) return;
        if (!mediaPlayer.isPlaying()) {
            if (playStatus == 3) {//停止播放后，再次播放需prepareAsync
                mediaPlayer.prepareAsync();
            }
            mediaPlayer.start();
            playStatus = 1;
        }
    }

    @Override
    public void pause() {//暂停
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playStatus = 2;
        }
    }


    @Override
    public void stop() {//停止播放
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        playStatus = 3;
    }

    @Override
    public void seekToPosition(int position) {
        if (mediaPlayer == null) return;
        int duration = getDuration();
        if (position > duration) return;
        mediaPlayer.seekTo(position);
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getDuration();
    }

    public int getPlayStatus() {
        return playStatus;
    }
}