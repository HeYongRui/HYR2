package testcomponent.heyongrui.com.base.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by lambert on 2018/10/23.
 */

public class RxManager {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();// 管理订阅者者

    public void add(Disposable m) {
        compositeDisposable.add(m);
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
