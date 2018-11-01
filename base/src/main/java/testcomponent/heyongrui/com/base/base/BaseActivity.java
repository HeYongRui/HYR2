package testcomponent.heyongrui.com.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Brian Wu on 2017/4/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public RxManager mRxManager = new RxManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        init(savedInstanceState);
    }

    public abstract int getContentViewLayoutId();

    public abstract void init(Bundle savedInstanceState);

    protected void addOnClickListeners(View.OnClickListener onClickListener, @IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener(onClickListener);
            }
        }
    }

    protected void launchActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void launchActivity(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
