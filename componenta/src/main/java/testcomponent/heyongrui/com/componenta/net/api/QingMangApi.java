package testcomponent.heyongrui.com.componenta.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lambert on 2018/11/2.
 */

public interface QingMangApi {

    @GET("v2/apps/box.proto")
    Observable<ResponseBody> getQingMang(@Query("v") String version,
                                         @Query("udid") String udid,
                                         @Query("vc") int vc);
}
