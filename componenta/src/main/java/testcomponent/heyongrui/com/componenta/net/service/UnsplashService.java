package testcomponent.heyongrui.com.componenta.net.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import testcomponent.heyongrui.com.base.network.configure.RxHelper;
import testcomponent.heyongrui.com.base.network.service.ApiService;
import testcomponent.heyongrui.com.componenta.configure.KeyManager;
import testcomponent.heyongrui.com.componenta.net.api.UnsplashApi;
import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;

/**
 * Created by lambert on 2018/10/23.
 */

public class UnsplashService {
    private final String BASE_URL_UNSPLASH = "https://api.unsplash.com/";

    @Inject
    public UnsplashService() {

    }

    public Observable<List<UnsplashPicDto>> getRandomPic(int page, int per_page) {
        return ApiService.createApi(UnsplashApi.class, BASE_URL_UNSPLASH)
                .getRandomPic(KeyManager.UNSPLASH_ACCESS_KEY, page, per_page)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }

    public Observable<List<UnsplashPicDto>> getRandomPicMap(int page, int per_page) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("client_id", KeyManager.UNSPLASH_ACCESS_KEY);
        fields.put("page", page);
        fields.put("per_page", per_page);
        return ApiService.createApi(UnsplashApi.class, BASE_URL_UNSPLASH)
                .getRandomPicMap(fields)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }
}
