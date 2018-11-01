package testcomponent.heyongrui.com.base.network.interceptor;

import android.os.Build;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import testcomponent.heyongrui.com.base.util.CoreUtil;

/**
 * Created by Brian Wu on 2016/6/1.
 */
public class HeaderInterceptor implements Interceptor {
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    public static final String USER_AGENT = "User-Agent";
    private SPUtils latlon;
    private SPUtils spUtils;

    public HeaderInterceptor() {
        spUtils = SPUtils.getInstance("app_config");
        latlon = SPUtils.getInstance("latlon");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String ipAddress = NetworkUtils.getIPAddress(true);
        if (HTTP_POST.equals(originalRequest.method())) {
            Request.Builder requestBuilder = originalRequest.newBuilder();
            if (!StringUtils.isTrimEmpty(ipAddress)) {
                requestBuilder.addHeader("x-rip", ipAddress);
            }
            originalRequest = requestBuilder
                    .addHeader("x-app-version", AppUtils.getAppVersionName() + "")
                    .addHeader("x-channel", "1")
                    .addHeader("x-device-os", "Android")
                    .addHeader("x-device-type", Build.VERSION.RELEASE)
                    .addHeader("x-device-name", Build.MODEL)
                    .addHeader("x-device-token", latlon.getString("device_token", ""))
                    .addHeader("x-user-uuid", latlon.getString("device_token", ""))
                    .addHeader("x-coordinate-type", "0")
                    .addHeader("x-lat", latlon.getString("lat", "0.0"))
                    .addHeader("x-lon", latlon.getString("lon", "0.0"))
                    .addHeader("x-theme-type", spUtils.getString("advert_group", "0"))
                    .addHeader(USER_AGENT, CoreUtil.getUserAgent()).post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                            URLDecoder.decode(CoreUtil.bodyToString(originalRequest.body()), "UTF-8")))
                    .build();
        } else {
            Request.Builder requestBuilder = originalRequest.newBuilder();
            if (!StringUtils.isTrimEmpty(ipAddress)) {
                requestBuilder.addHeader("x-rip", ipAddress);
            }
            originalRequest = requestBuilder
                    .addHeader("x-app-version", AppUtils.getAppVersionName() + "")
                    .addHeader("x-channel", "1")
                    .addHeader("x-device-os", "Android")
                    .addHeader("x-device-type", android.os.Build.VERSION.RELEASE)
                    .addHeader("x-device-name", android.os.Build.MODEL)
                    .addHeader("x-device-token", latlon.getString("device_token", ""))
                    .addHeader("x-user-uuid", latlon.getString("device_token", ""))
                    .addHeader("x-coordinate-type", "0")
                    .addHeader("x-lat", latlon.getString("lat", "0.0"))
                    .addHeader("x-lon", latlon.getString("lon", "0.0"))
                    .addHeader("x-theme-type", spUtils.getString("advert_group", "0"))
                    .addHeader(USER_AGENT, CoreUtil.getUserAgent()).get()
                    .build();
        }
        return chain.proceed(originalRequest);
    }
}
