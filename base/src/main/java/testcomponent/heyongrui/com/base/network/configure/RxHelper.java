package testcomponent.heyongrui.com.base.network.configure;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import testcomponent.heyongrui.com.base.network.core.CoreApiException;
import testcomponent.heyongrui.com.base.network.core.CoreHeader;
import testcomponent.heyongrui.com.base.network.core.CoreResponse;


/**
 * Created by hpw on 16/11/2.
 */

public class RxHelper {

    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<CoreResponse<T>, T> handleResult() {
        return upstream -> {
            return upstream.flatMap(tCoreResponse -> {
                        CoreHeader header = tCoreResponse.getHeader();
                        int responseCode = header == null ? -1 : header.getStatus();
                        if (responseCode == 200) {
                            return createData(tCoreResponse.getData());
                        } else {
                            return Observable.error(new CoreApiException(responseCode, tCoreResponse.getHeader().getMsg()));
                        }
                    }
            );
        };
    }

    private static <T> Observable<T> createData(final T t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * 倒计时
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(increaseTime -> countTime - increaseTime.intValue())
                .take(countTime + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
