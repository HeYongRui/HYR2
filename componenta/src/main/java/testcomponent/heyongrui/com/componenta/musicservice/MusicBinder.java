package testcomponent.heyongrui.com.componenta.musicservice;

import android.media.MediaPlayer;
import android.os.Binder;

import java.io.IOException;

/**
 * Created by lambert on 2018/11/22.
 */

public class MusicBinder extends Binder implements MusicOperation {

    private MusicService musicService;
    private MediaPlayer mediaPlayer;

    public MusicBinder(MediaPlayer mediaPlayer, MusicService musicService) {
        this.mediaPlayer = mediaPlayer;
        this.musicService = musicService;
    }

    @Override
    public void startPlay(String url) {//播放新的歌曲
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();//网络视频，异步
            if (musicService != null) {
                musicService.setPlayStatus(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {//播放
        if (mediaPlayer == null) return;
        if (!mediaPlayer.isPlaying()) {
            if (musicService != null) {
                if (musicService.getPlayStatus() == 3) {//停止播放后，再次播放需prepareAsync
                    mediaPlayer.prepareAsync();
                }
            }
            mediaPlayer.start();
            if (musicService != null) {
                musicService.setPlayStatus(1);
            }
        }
    }

    @Override
    public void pause() {//暂停
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (musicService != null) {
                musicService.setPlayStatus(2);
            }
        }
    }


    @Override
    public void stop() {//停止播放
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        if (musicService != null) {
            musicService.setPlayStatus(3);
        }
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
        if (mediaPlayer.isPlaying()) {
            if (musicService != null) {
                musicService.setPlayStatus(1);
            }
        }
        return mediaPlayer.getDuration();
    }

    public int getPlayStatus() {
        if (musicService == null) return 0;
        return musicService.getPlayStatus();
    }

    public int getPlayItemPosition() {
        if (musicService == null) return 0;
        return musicService.getPlayItemPosition();
    }

    public void setPlayItemPosition(int playItemPosition) {
        if (musicService == null) return;
        musicService.setPlayItemPosition(playItemPosition);
    }
}