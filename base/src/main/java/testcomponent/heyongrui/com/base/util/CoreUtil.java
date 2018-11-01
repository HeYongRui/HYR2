package testcomponent.heyongrui.com.base.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by lambert on 2018/6/8.
 */

public class CoreUtil {
    private static Context context;

    public static void init(@NonNull Context context) {
        //在此初始化全局context
        CoreUtil.context = context.getApplicationContext();
    }

    private CoreUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    public static String getAuthToken() {
        SPUtils spUtils = SPUtils.getInstance("http_request");
        String authToken = spUtils.getString("http_request_auth_token", "tokenid_undefined");
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        SPUtils spUtils = SPUtils.getInstance("http_request");
        spUtils.put("http_request_auth_token", authToken);
    }

    public static String getAuthUserId() {
        SPUtils spUtils = SPUtils.getInstance("http_request");
        String authToken = spUtils.getString("http_request_auth_userid", "1");
        return authToken;
    }

    public static void setAuthUserId(String authUserId) {
        SPUtils spUtils = SPUtils.getInstance("http_request");
        spUtils.put("http_request_auth_userid", authUserId);
    }

    /**
     * 获得请求的服务端数据的userAgent
     *
     * @return
     */
    public static String getUserAgent() {
        StringBuilder ua = new StringBuilder("findmacau.com");
        ua.append('/' + AppUtils.getAppInfo(AppUtils.getAppPackageName()).getVersionName() + '_'
                + AppUtils.getAppInfo(AppUtils.getAppPackageName()).getVersionCode());// app版本信息
        ua.append("/Android");// 手机系统平台
        ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        ua.append("/" + android.os.Build.MODEL); // 手机型号
        ua.append("/" + CoreUtil.getSid());// 客户端唯一标识
        return ua.toString();
    }

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    /**
     * 获得设备ID
     *
     * @return
     */
    public static String getSid() {
        SPUtils spUtils = SPUtils.getInstance("device_info");
        String sid = spUtils.getString("device_info_sid");
        if (StringUtils.isEmpty(sid)) {
            sid = UUID.randomUUID().toString();
            spUtils.put("device_info_sid", sid);
        }
        return sid;
    }
}
