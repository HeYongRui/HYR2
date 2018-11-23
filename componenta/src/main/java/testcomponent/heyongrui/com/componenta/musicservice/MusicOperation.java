package testcomponent.heyongrui.com.componenta.musicservice;

/**
 * Created by lambert on 2018/11/22.
 */

public interface MusicOperation {
    void startPlay(String url);

    void play();

    void pause();

    void stop();

    void seekToPosition(int position);

    int getCurrentPosition();

    int getDuration();//获取总时长
}