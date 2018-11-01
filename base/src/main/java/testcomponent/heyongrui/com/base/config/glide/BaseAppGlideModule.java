package testcomponent.heyongrui.com.base.config.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by lambert on 2018/10/26.
 */

@GlideModule
public class BaseAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        //覆写缓存大小
        int memoryCacheSizeBytes = 1024 * 1024 * 20;//20Mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //覆写BitmapPool的大小
        int bitmapPoolSizeBytes = 1024 * 1024 * 20;//20Mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
        //覆写磁盘缓存的大小
        int diskCacheSizeBytes = 1024 * 1024 * 50;//50Mb
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}
