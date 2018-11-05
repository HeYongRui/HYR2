package testcomponent.heyongrui.com.base.network.interceptor;

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

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String host = originalRequest.url().host();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        String method = originalRequest.method();
        switch (method) {
            case "POST":
                originalRequest = requestBuilder
                        .addHeader("Cookie", "wdj_auth=_V3V0VDSEFUX29TV2R4dHhiLVlSRlNzcHJwQ1Z4SGtyMVN1VzBfMTUwNjc1NDgxNDI1MToxNTcyNjc3ODA3MzA3OmE1ZjZiYzU3ZTE4NjQ1YWQ5ZWNlMmExYjgxYzE0OWVl")
                        .addHeader("Accept-Charset", "utf-8")
                        .addHeader("HTTP-AUTHORIZATION", "f1cbda8fa5a711e7b87f5254001b74f1")
                        .addHeader("User-Agent", CoreUtil.getUserAgent()).post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                                URLDecoder.decode(CoreUtil.bodyToString(originalRequest.body()), "UTF-8")))
                        .build();
                break;
            case "GET":
                originalRequest = requestBuilder
                        .addHeader("Cookie", "wdj_auth=_V3V0VDSEFUX29TV2R4dHhiLVlSRlNzcHJwQ1Z4SGtyMVN1VzBfMTUwNjc1NDgxNDI1MToxNTcyNjc3ODA3MzA3OmE1ZjZiYzU3ZTE4NjQ1YWQ5ZWNlMmExYjgxYzE0OWVl")
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("Accept-Charset", "utf-8")
                        .addHeader("HTTP-AUTHORIZATION", "f1cbda8fa5a711e7b87f5254001b74f1")
//                    .addHeader("Accept-Encoding", "gzip")
                        .addHeader("User-Agent", CoreUtil.getUserAgent()).get()
                        .build();
                break;
        }
        return chain.proceed(originalRequest);
    }
}
