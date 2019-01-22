package testcomponent.heyongrui.com.base.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    public RxManager mRxManager = new RxManager();
    protected Context mContext;
    protected View mView;

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            int layoutId = getLayoutId();
            if (layoutId == 0) {
                View layoutView = getLayoutView();
                if (layoutView != null) {
                    mView = layoutView;
                } else {
                    try {
                        throw new Exception("please set a layoutId or layoutView");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mView = inflater.inflate(layoutId, container, false);
            }
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

    protected void addOnLongClickListeners(View.OnLongClickListener onClickListener, @IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                mView.findViewById(id).setOnLongClickListener(onClickListener);
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

    protected View getLayoutView() {
        return null;
    }

    protected abstract void init(Bundle savedInstanceState);

    protected void onFirstUserVisible() {

    }

    protected void onUserVisible() {

    }

    protected void onFirstUserInvisible() {

    }

    protected void onUserInvisible() {

    }

    @Override
    public void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }
}
