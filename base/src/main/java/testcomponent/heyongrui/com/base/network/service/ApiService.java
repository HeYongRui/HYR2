package testcomponent.heyongrui.com.base.network.service;


import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import testcomponent.heyongrui.com.base.BuildConfig;
import testcomponent.heyongrui.com.base.network.configure.HttpCache;
import testcomponent.heyongrui.com.base.network.configure.TrustManager;
import testcomponent.heyongrui.com.base.network.interceptor.CacheInterceptor;
import testcomponent.heyongrui.com.base.network.interceptor.HeaderInterceptor;
import testcomponent.heyongrui.com.base.network.interceptor.HttpLogInterceptor;
import testcomponent.heyongrui.com.base.network.interceptor.HttpLogger;
import testcomponent.heyongrui.com.base.network.interceptor.TokenInterceptor;

/**
 * Created by Brian Wu on 2016/12/15.
 */

public class ApiService {
//    public static final String DOMAIN_NAME_HEADER = "Domain-Name: ";
//    public static final String DOMAIN_NAME = "base_api_url_user";
    public static final String BASE_API_URL_FORMAL = "https://api.findmacau.com/fm/";//用户版API正式环境
    public static final String BASE_API_URL_TEST = "https://api.findmacau.com/uat/fm/";//用户版API测试环境

    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;

    private static final HttpLogInterceptor formattingJsonLogInterceptor = new HttpLogInterceptor(new HttpLogger()).setLevel(HttpLogInterceptor.Level.BODY);
    private static HeaderInterceptor headerInterceptor = new HeaderInterceptor();
    private static TokenInterceptor tokenInterceptor = new TokenInterceptor();
    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())//SSL证书
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .addInterceptor(headerInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(formattingJsonLogInterceptor)
            .cache(HttpCache.getCache())
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(cacheInterceptor)
            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)//TimeOut
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)//失败重连
            .build();

    public static <T> T createApi(Class<T> clazz) {
        return createApi(clazz, null);
    }

    public static <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (TextUtils.isEmpty(baseUrl)) {
            if (BuildConfig.DEBUG) {
                builder.baseUrl(BASE_API_URL_TEST);
            } else {
                builder.baseUrl(BASE_API_URL_FORMAL);
            }
        } else {
            builder.baseUrl(baseUrl);
        }
        builder.client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(clazz);
    }
}

