package testcomponent.heyongrui.com.componenta.net.api;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangArticleListDto;
import testcomponent.heyongrui.com.componenta.net.dto.QingMangCategoriesDto;

/**
 * Created by lambert on 2018/11/2.
 */

public interface QingMangApi {

    @GET("category.list")
    Observable<QingMangCategoriesDto> getQingMangCategories(@Query("token") String token);

    @GET("article.list")
    Observable<QingMangArticleListDto> getQingMangArticleList(@QueryMap Map<String, String> fields);
}
