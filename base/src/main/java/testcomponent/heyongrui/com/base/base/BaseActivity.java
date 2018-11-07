package testcomponent.heyongrui.com.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Brian Wu on 2017/4/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public RxManager mRxManager = new RxManager();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId == 0) {
            View layoutView = getLayoutView();
            if (layoutView != null) {
                setContentView(layoutView);
            } else {
                try {
                    throw new Exception("please set a layoutId or layoutView");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            setContentView(layoutId);
        }
        init(savedInstanceState);
    }

    public abstract int getLayoutId();

    protected View getLayoutView() {
        return null;
    }

    public abstract void init(Bundle savedInstanceState);

    protected void launchActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void launchActivity(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }


    protected void addOnClickListeners(View.OnClickListener onClickListener, @IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener(onClickListener);
            }
        }
    }

    protected void addFragment(int frameLayoutId, Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment, tag);
                } else {
                    transaction.add(frameLayoutId, fragment, tag);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment, tag);
//            transaction.addSharedElement();//共享元素动效，API21+
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStackImmediate();
            ActivityCompat.finishAfterTransition(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
