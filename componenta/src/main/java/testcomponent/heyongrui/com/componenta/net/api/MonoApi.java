package testcomponent.heyongrui.com.componenta.net.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public interface MonoApi {

    @GET("tea/{query_date}/full/")
    Observable<MonoTeaDto> getTea(@Path("query_date") String query_date);
}
