package testcomponent.heyongrui.com.base.config.glide;

/**
 * Created by lambert on 2018/10/26.
 */

public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}