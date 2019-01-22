package testcomponent.heyongrui.com.base.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import testcomponent.heyongrui.com.base.util.UiUtil;

/**
 * Created by Brian Wu on 2017/4/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public RxManager mRxManager = new RxManager();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
            Log.i("onCreate", "onCreate fixOrientation when Oreo, result = " + result);
        }
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

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            Log.i("setRequestedOrientation", "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
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

    protected void adaptiveVirtualKeyboard(boolean isAdaptiveVirtualKeyboard) {//适配虚拟键盘机型
        if (isAdaptiveVirtualKeyboard) {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            boolean isHasNavigationBar = UiUtil.isHasNavigationBar(this);
            if (isHasNavigationBar) {//有虚拟键盘
                boolean navigationBarShow = UiUtil.isNavigationBarShow(this);
                if (navigationBarShow) {//虚拟键盘展示了就设置边距高度
                    int navigationBarHeight = UiUtil.getNavigationBarHeight(this);
                    rootView.setPadding(0, 0, 0, navigationBarHeight);
                }
            }
        }
    }

    protected void hideBottomVirtualKeyboard() {
        boolean isHasNavigationBar = UiUtil.isHasNavigationBar(this);
        if (!isHasNavigationBar) return;
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void addOnClickListeners(View.OnClickListener onClickListener, @IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener(onClickListener);
            }
        }
    }

    protected void addOnLongClickListeners(View.OnLongClickListener onClickListener, @IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnLongClickListener(onClickListener);
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
