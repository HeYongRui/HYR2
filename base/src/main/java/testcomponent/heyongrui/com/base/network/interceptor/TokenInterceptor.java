package testcomponent.heyongrui.com.base.network.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import testcomponent.heyongrui.com.base.util.CoreUtil;

/**
 * Created by Brian Wu on 2016/6/1.
 */
public class TokenInterceptor implements Interceptor {
    public static final String REQUEST_AUTH_TOKEN = "x-auth-token";
//    public String phoneLoginUrl = RxService.BASE_API_URL_USER + "auth/login/phone";
//    public String emailLoginUrl = RxService.BASE_API_URL_USER + "auth/login/email";
//    public String phoneRegistUrl = RxService.BASE_API_URL_USER + "auth/regist/phone";
//    public String emailRegistUrl = RxService.BASE_API_URL_USER + "auth/regist/email";
//    public String thirdPlatformUrl = RxService.BASE_API_URL_USER + "auth/login/third_platform";
//    public String restpasswordUrl = RxService.BASE_API_URL_USER + "auth/rest_password/sms_code";
//    public String adminloginUrl = RxService.BASE_API_URL_MANAGER + "admin/auth/login";

    public String phoneLoginUrl = "auth/login/phone";
    public String emailLoginUrl = "auth/login/email";
    public String phoneRegistUrl = "auth/regist/phone";
    public String emailRegistUrl = "auth/regist/email";
    public String thirdPlatformUrl = "auth/login/third_platform";
    public String restpasswordUrl = "auth/rest_password/sms_code";
    public String adminloginUrl = "admin/auth/login";
    public String storeadminloginUrl = "store_verification/auth/login";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String originalToken = CoreUtil.getAuthToken();
        String url = originalRequest.url().toString();
        Request authRequest = null;

//        if (url.equals(phoneLoginUrl) || url.equals(adminloginUrl) || url.equals(emailLoginUrl)
//                || url.equals(phoneRegistUrl) || url.equals(emailRegistUrl)) {
//            authRequest = originalRequest;
//        } else {
//            //传入token
//            authRequest = originalRequest.newBuilder().addHeader(REQUEST_AUTH_TOKEN, originalToken).build();
//        }
//        Response originalResponse = chain.proceed(authRequest);
//        //保存token
//        if (url.equals(phoneLoginUrl) || url.equals(phoneRegistUrl) || url.equals(emailLoginUrl) || url.equals(emailRegistUrl)
//                || url.equals(thirdPlatformUrl) || url.equals(restpasswordUrl) || url.equals(adminloginUrl)) {
//            String authToken = originalResponse.header(REQUEST_AUTH_TOKEN);
//            appContext.setAuthToken(authToken);
//        }

        //测试时切换BaseUrl可能导致BASE_API_URL_USER不同，所以只判断是否包含接口名称
        if (url.contains(phoneLoginUrl) || url.contains(adminloginUrl) || url.contains(emailLoginUrl)
                || url.contains(phoneRegistUrl) || url.contains(emailRegistUrl)
                || url.contains(storeadminloginUrl)) {
            authRequest = originalRequest;
        } else {
            //传入token
            authRequest = originalRequest.newBuilder().addHeader(REQUEST_AUTH_TOKEN, originalToken).build();
        }
        Response originalResponse = chain.proceed(authRequest);
        //保存token
        if (url.contains(phoneLoginUrl) || url.contains(phoneRegistUrl) || url.contains(emailLoginUrl) || url.contains(emailRegistUrl)
                || url.contains(thirdPlatformUrl) || url.contains(restpasswordUrl) || url.contains(adminloginUrl)
                || url.contains(storeadminloginUrl)) {
            String authToken = originalResponse.header(REQUEST_AUTH_TOKEN);
            CoreUtil.setAuthToken(authToken);
        }

        return originalResponse;
    }
}
