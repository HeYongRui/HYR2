package testcomponent.heyongrui.com.componenta.net.service;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import testcomponent.heyongrui.com.base.network.configure.RxHelper;
import testcomponent.heyongrui.com.base.network.service.ApiService;
import testcomponent.heyongrui.com.base.util.StringUtil;
import testcomponent.heyongrui.com.componenta.net.api.QingMangApi;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangArticleListDto;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;

/**
 * Created by lambert on 2018/11/2.
 */

public class QingMangService {

    private final String BASE_URL_QINGMANG = "https://api.qingmang.me/v2/";
    private final String QINGMANG_TOKEN = "c400a7e21688496ca3e7f17c6b0d1846";

    @Inject
    public QingMangService() {

    }

    public Observable<QingMangCategoriesDto> getQingMangCategories() {
        return ApiService.createApi(QingMangApi.class, BASE_URL_QINGMANG)
                .getQingMangCategories(QINGMANG_TOKEN)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }

    public Observable<QingMangArticleListDto> getQingMangArticleList(String category_id, String next_url) {
        Map<String, String> fields = new HashMap<>();
        if (TextUtils.isEmpty(next_url)) {//指定参数(第一页)
            fields.put("token", QINGMANG_TOKEN);
            fields.put("category_id", category_id);
        } else {//提取下一页参数
            fields = StringUtil.getParmMap(next_url);
        }
        return ApiService.createApi(QingMangApi.class, BASE_URL_QINGMANG)
                .getQingMangArticleList(fields)
                .compose(RxHelper.rxSchedulerHelper());
//                .compose(RxHelper.handleResult());
    }
}
