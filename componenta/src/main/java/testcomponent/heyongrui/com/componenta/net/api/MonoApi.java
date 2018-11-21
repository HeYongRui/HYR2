package testcomponent.heyongrui.com.componenta.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import testcomponent.heyongrui.com.componenta.net.dto.MonoCategoryDto;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public interface MonoApi {

    /**
     * 早茶
     */
    @GET("api/v3/tea/{query_date}/full/")
    Observable<MonoTeaDto> getTea(@Path("query_date") String query_date);

    @GET("api/v3/domain_category/{category_id}/")
    Observable<MonoCategoryDto> getCategory(@Path("category_id") int category_id, @Query("start") Integer start);

    @GET("g/meow/{meow_id}/ ")
    Observable<ResponseBody> getMeowDetail(@Path("meow_id") int meow_id);
}
