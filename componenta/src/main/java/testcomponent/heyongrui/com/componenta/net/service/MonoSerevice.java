package testcomponent.heyongrui.com.componenta.net.service;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import testcomponent.heyongrui.com.base.network.configure.RxHelper;
import testcomponent.heyongrui.com.base.network.service.ApiService;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.componenta.net.api.MonoApi;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoSerevice {
    private final String BASE_URL_MONO = "http://mmmono.com/api/v3/";

    @Inject
    public MonoSerevice() {

    }

    public Observable<MonoTeaDto> getTea()  {
        String dateString = null;
        try {
            dateString = TimeUtil.getDateString(new Date(), TimeUtil.DAY_ONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiService.createApi(MonoApi.class, BASE_URL_MONO)
                .getTea(dateString)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }
}
