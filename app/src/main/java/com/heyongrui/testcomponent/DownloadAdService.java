package com.heyongrui.testcomponent;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import testcomponent.heyongrui.com.base.util.FileUtil;

/**
 * Created by lambert on 2018/10/24.
 */

public class DownloadAdService extends IntentService {

    public DownloadAdService() {
        super("DownloadAdService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//兼容android8.0
            startForeground(1, new Notification());
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // IntentService会使用单独的线程来执行该方法的代码
        // 该方法内执行耗时任务，比如下载文件
        if (intent == null) return;
        //获取传过来的下载地址和最新广告模型
        String download_url = intent.getStringExtra("download_url");
        String save_path = intent.getStringExtra("save_path");
        String file_name = intent.getStringExtra("file_name");
        if (StringUtils.isTrimEmpty(download_url)) return;
        if (TextUtils.isEmpty(file_name)) file_name = "splash.jpg";
        downloadFile(download_url, save_path, file_name);
    }

    public void downloadFile(String download_url, String save_path, String file_name) {//异步下载文件并保存到本地
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(download_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //储存下载文件的目录(APP对应的缓存目录)
                File cacheDirectory = FileUtil.getCacheDirectory(DownloadAdService.this, null);
                if (cacheDirectory == null || !cacheDirectory.exists()) return;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = null;
                    if (TextUtils.isEmpty(save_path)) {
                        file = new File(cacheDirectory.getPath(), file_name);
                    } else {
                        file = new File(save_path, file_name);
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        // 下载中
                        String progress = String.format("%.2f", (((float) sum / (float) total) * 100)) + "%";
                        Log.d("下载进度：", progress);
                    }
                    fos.flush();
                    // 下载完成
                    Log.d("下载完成：", "文件路径：" + file.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}