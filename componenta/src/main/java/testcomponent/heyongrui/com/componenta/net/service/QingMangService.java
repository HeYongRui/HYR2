package testcomponent.heyongrui.com.componenta.net.service;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import testcomponent.heyongrui.com.base.network.configure.RxHelper;
import testcomponent.heyongrui.com.base.network.service.ApiService;
import testcomponent.heyongrui.com.componenta.net.api.QingMangApi;

/**
 * Created by lambert on 2018/11/2.
 */

public class QingMangService {

    private final String BASE_URL_QINGMANG = "http://ripple.qingmang.me/api/";

    @Inject
    public QingMangService() {

    }

    public Observable<ResponseBody> getQingMang(String version, String udid, int vc) {
        return ApiService.createApi(QingMangApi.class, BASE_URL_QINGMANG)
                .getQingMang(version, udid, vc)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }
}
