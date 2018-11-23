package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.base.util.DrawableUtil;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.base.widget.itemdecoration.RecycleViewItemDecoration;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.component.MusicIDynamicComponent;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.musicservice.MusicBinder;
import testcomponent.heyongrui.com.componenta.musicservice.MusicService;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTabDto;
import testcomponent.heyongrui.com.componenta.net.service.MonoSerevice;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoAdapter;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoMultipleItem;

/**
 * Created by lambert on 2018/11/22.
 */

public class MonoTabActivity extends BaseActivity {

    private MonoAdapter mMonoAdapter;
    private int mPage;
    private int mPerPage = 10;
    private boolean mIsLastPage;

    @Inject
    MonoSerevice mMonoSerevice;

    private MusicBinder mBinder;
    private ServiceConnection mServiceConnection;
    private MusicIDynamicComponent musicIDynamicComponent;
    private int mLastPlayPosition = -1;
    private int mPlayStatus;//播放状态 0-未开始 1-播放中 2-暂停

    public static void launchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MonoTabActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mono_tab;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initInject();
        initMusicService();
        initView();
        getMusicTab(true);
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(this))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }

    private void initView() {
        ImageView backIv = findViewById(R.id.back_iv);
        Drawable drawable = DrawableUtil.tintDrawable(this, R.drawable.back, R.color.white);
        backIv.setImageDrawable(drawable);
        addOnClickListeners(view -> {
            int id = view.getId();
            if (id == R.id.back_iv) {
                finish();
            }
        }, R.id.back_iv);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        StoreHouseHeader storeHouseHeader = findViewById(R.id.storeHouseHeader);
        initSwipeRefresh(refreshLayout, storeHouseHeader);
    }

    private void initSwipeRefresh(SmartRefreshLayout smartRefreshLayout, StoreHouseHeader storeHouseHeader) {
        storeHouseHeader.setTextColor(ContextCompat.getColor(this, R.color.white));
        storeHouseHeader.setPrimaryColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        smartRefreshLayout.setEnableNestedScroll(true);
        smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
                if (mIsLastPage) {
                    refreshlayout.setLoadmoreFinished(true);
                } else {
                    getMusicTab(false);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                refreshlayout.finishRefresh();
                refreshlayout.setLoadmoreFinished(false);
                getMusicTab(true);
                mBinder.stop();
                mPlayStatus = 0;
                resetPlayStatusUI(mLastPlayPosition);
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        List<MonoMultipleItem> data = new ArrayList<>();
        mMonoAdapter = new MonoAdapter(data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //防止Recyclerview局部刷新item闪烁
        ((SimpleItemAnimator) recyclerView.getItemAnimator())
                .setSupportsChangeAnimations(false);
        mMonoAdapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewItemDecoration(this, 1));
        mMonoAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.play_iv) {
                MonoTabDto.EntityListBean tabEntityListBean = mMonoAdapter.getData().get(position).getTabEntityListBean();
                if (tabEntityListBean == null) return;
                MonoTabDto.EntityListBean.MeowBean meow = tabEntityListBean.getMeow();
                if (meow == null) return;
                String music_url = meow.getMusic_url();
                if (mLastPlayPosition == position) {
                    //点击是同一个item，进行播放状态的切换
                    switch (mPlayStatus) {
                        case 0://未开始/播放完毕/停止播放
                            if (TextUtils.isEmpty(music_url)) return;
                            mLastPlayPosition = position;
                            mBinder.startPlay(music_url);
                            mPlayStatus = 1;
                            updatePlayIcon(position, false);
                            break;
                        case 1://播放中
                            mBinder.pause();
                            mPlayStatus = 2;
                            updatePlayIcon(position, true);
                            break;
                        case 2://暂停
                            mBinder.play();
                            mPlayStatus = 1;
                            updatePlayIcon(position, false);
                            break;
                    }
                } else {//点击的不是同一个item，切歌
                    resetPlayStatusUI(mLastPlayPosition);//切歌之前先把上一个item的UI状态重置
                    //进行播放新歌并更新UI
                    mLastPlayPosition = position;
                    mBinder.startPlay(music_url);
                    mPlayStatus = 1;
                    updatePlayIcon(position, false);
                }
            }
        });
    }

    private void initMusicService() {
        //初始化绑定音乐服务
        Intent intent = new Intent(this, MusicService.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBinder = (MusicBinder) iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        //动态注册音乐状态回调组件
        musicIDynamicComponent = new MusicIDynamicComponent();
        musicIDynamicComponent.setMusicCallBack((currentPosition, duration) -> {
            //音乐播放回调(每隔一秒回调),更新UI
            String currentPosition_string = TimeUtil.msToString(currentPosition, TimeUtil.MIN_SEC);
            if (mLastPlayPosition < 0) return;
            ProgressBar progressbar = (ProgressBar) mMonoAdapter.getViewByPosition(mLastPlayPosition, R.id.progressbar);
            TextView durationTv = (TextView) mMonoAdapter.getViewByPosition(mLastPlayPosition, R.id.duration_tv);
            ImageView playIv = (ImageView) mMonoAdapter.getViewByPosition(mLastPlayPosition, R.id.play_iv);
            if (duration == currentPosition) {//播放完成
                playIv.setImageResource(R.drawable.play);
                if (progressbar != null) {
                    progressbar.setMax(100);
                    progressbar.setProgress(0);
                }
                if (durationTv != null) {
                    MonoTabDto.EntityListBean.MeowBean lastMeow = mMonoAdapter.getData().get(mLastPlayPosition).getTabEntityListBean().getMeow();
                    String music_duration = TimeUtil.msToString(lastMeow.getMusic_duration() * 1000, TimeUtil.MIN_SEC);
                    durationTv.setText(music_duration);
                }
                mPlayStatus = 0;
            } else if (duration > currentPosition) {//播放中
                if (progressbar != null) {
                    progressbar.setMax(duration);
                    progressbar.setProgress(currentPosition);
                }
                if (durationTv != null) {
                    durationTv.setText(currentPosition_string);
                }
            }
        });
        CC.registerComponent(musicIDynamicComponent);
    }

    private void getMusicTab(boolean isClearData) {
        mPage = isClearData ? 0 : mPage + 1;
        mRxManager.add(mMonoSerevice.getMusicTab(mPage, mPerPage)
                .subscribeWith(new ResponseDisposable<MonoTabDto>(this) {
                    @Override
                    protected void onSuccess(MonoTabDto monoTabDto) {
                        if (monoTabDto == null) return;
                        mIsLastPage = monoTabDto.isIs_last_page();
                        if (isClearData) {
                            mMonoAdapter.getData().clear();
                        }
                        List<MonoTabDto.EntityListBean> entity_list = monoTabDto.getEntity_list();
                        if (entity_list == null || entity_list.isEmpty()) return;
                        for (MonoTabDto.EntityListBean entityListBean : entity_list) {
                            mMonoAdapter.addData(new MonoMultipleItem(MonoMultipleItem.TYPE_TAB_MUSIC, entityListBean));
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }

    private void updatePlayIcon(int position, boolean isPlaying) {//更新播放按钮
        if (position < 0) return;
        if (mMonoAdapter == null) return;
        ImageView playIv = (ImageView) mMonoAdapter.getViewByPosition(position, R.id.play_iv);
        if (playIv != null) {
            if (isPlaying) {
                playIv.setImageResource(R.drawable.play);
            } else {
                playIv.setImageResource(R.drawable.pause);
            }
        }
    }

    private void resetPlayStatusUI(int position) {//重置item的播放UI
        if (position < 0) return;
        if (mMonoAdapter == null) return;
        ProgressBar progressbar = (ProgressBar) mMonoAdapter.getViewByPosition(position, R.id.progressbar);
        ImageView playIv = (ImageView) mMonoAdapter.getViewByPosition(position, R.id.play_iv);
        TextView durationTv = (TextView) mMonoAdapter.getViewByPosition(position, R.id.duration_tv);
        if (progressbar != null) {
            progressbar.setMax(100);
            progressbar.setProgress(0);
        }
        if (playIv != null) {
            playIv.setImageResource(R.drawable.play);
        }
        if (durationTv != null) {
            List<MonoMultipleItem> monoAdapterData = mMonoAdapter.getData();
            if (monoAdapterData != null) {
                MonoMultipleItem monoMultipleItem = monoAdapterData.get(position);
                if (monoMultipleItem != null) {
                    MonoTabDto.EntityListBean tabEntityListBean = monoMultipleItem.getTabEntityListBean();
                    if (tabEntityListBean != null) {
                        MonoTabDto.EntityListBean.MeowBean meow = tabEntityListBean.getMeow();
                        if (meow != null) {
                            String music_duration = TimeUtil.msToString(meow.getMusic_duration() * 1000, TimeUtil.MIN_SEC);
                            durationTv.setText(music_duration);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CC.unregisterComponent(musicIDynamicComponent);
        unbindService(mServiceConnection);
        mServiceConnection = null;
    }
}
