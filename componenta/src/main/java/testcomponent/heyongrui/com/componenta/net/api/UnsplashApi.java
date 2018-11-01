package testcomponent.heyongrui.com.componenta.net.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;

/**
 * Created by lambert on 2018/10/23.
 */

public interface UnsplashApi {

    @GET("photos")
    Observable<List<UnsplashPicDto>> getRandomPic(@Query("client_id") String client_id,
                                                  @Query("page") int page,
                                                  @Query("per_page") int per_page);

    @GET("photos")
    Observable<List<UnsplashPicDto>> getRandomPicMap(@QueryMap Map<String, Object> fields);
}
