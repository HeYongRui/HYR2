package testcomponent.heyongrui.com.base.network.configure;


import android.os.Environment;

import java.io.File;

import okhttp3.Cache;
import testcomponent.heyongrui.com.base.util.CoreUtil;

/**
 * Created by Brian Wu on 2016/12/15.
 */
public class HttpCache {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;

    public static Cache getCache() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = CoreUtil.getContext().getExternalCacheDir().getPath();
        } else {
            cachePath = CoreUtil.getContext().getCacheDir().getPath();
        }

        File cacheFile = new File(cachePath);
        Cache cache = new Cache(cacheFile, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
        return cache;
    }
}
