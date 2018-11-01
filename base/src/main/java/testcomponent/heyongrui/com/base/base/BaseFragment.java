package testcomponent.heyongrui.com.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    public RxManager mRxManager = new RxManager();

    protected View mView;

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
            init(savedInstanceState);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    protected void addOnClickListeners(View.OnClickListener onClickListener, @IdRes int... ids) {
        if (mView == null) return;
        if (ids != null) {
            for (@IdRes int id : ids) {
                mView.findViewById(id).setOnClickListener(onClickListener);
            }
        }
    }

    protected void launchActivity(Class<?> clazz) {
        Intent intent = new Intent(getContext(), clazz);
        startActivity(intent);
    }

    protected void launchActivity(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getContext(), clazz);
        startActivityForResult(intent, requestCode);
    }

    protected abstract int getLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract void onFirstUserVisible();

    protected abstract void onUserVisible();

    protected abstract void onFirstUserInvisible();

    protected abstract void onUserInvisible();

    @Override
    public void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
