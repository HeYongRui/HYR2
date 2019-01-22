package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import testcomponent.heyongrui.com.base.BaseApp;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.config.glide.GlideApp;
import testcomponent.heyongrui.com.base.util.DrawableUtil;
import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.base.util.ImageUtil;
import testcomponent.heyongrui.com.base.util.StatusBarUtil;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.base.util.UiUtil;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.injection.component.DaggerComponentAActivityComponent;
import testcomponent.heyongrui.com.componenta.injection.module.ComponentAActivityModule;
import testcomponent.heyongrui.com.componenta.musicservice.MusicBinder;
import testcomponent.heyongrui.com.componenta.musicservice.MusicReceiver;
import testcomponent.heyongrui.com.componenta.musicservice.MusicService;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTabDto;

/**
 * Created by lambert on 2018/11/26.
 */

public class MonoMusicPlayActivity extends BaseActivity {

    private ImageView bgIv;
    private ImageView backIv;
    private TextView titleTv;
    private ImageView turntableIv;
    private ImageView albumIv;
    private ImageView stylusIv;
    private Group phonographGroup;
    private ScrollView lyricScl;
    private TextView lyricTv;
    private TextView playTimeTv;
    private TextView totalTimeTv;
    private SeekBar seekBar;
    private ImageView playIv;

    private AnimatorSet animatorSet;
    private MusicBinder mBinder;
    private ServiceConnection mServiceConnection;
    private MusicReceiver mReceiver;
    private List<MonoTabDto.EntityListBean> mEntityListBeans;
    private int mPosition;

    public static void launchActivity(Context context, List<MonoTabDto.EntityListBean> listBeans, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listBeans", (ArrayList<? extends Parcelable>) listBeans);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        intent.setClass(context, MonoMusicPlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mono_music_play;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        //隐藏虚拟键盘
        hideBottomVirtualKeyboard();
        //设置全屏模式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initInject();
        initView();
        initData();
        bindMusicService();
        registerMusicReceiver();
    }

    private void initInject() {
        DaggerComponentAActivityComponent.builder()
                .componentAActivityModule(new ComponentAActivityModule(this))
                .baseAppComponent(BaseApp.getInstance().getBaseAppComponent())
                .build().inject(this);
    }

    private void initView() {
        View toolBar = findViewById(R.id.tool_bar);
        bgIv = findViewById(R.id.bg_iv);
        View mask = findViewById(R.id.mask);
        View divide = findViewById(R.id.divide);
        backIv = findViewById(R.id.back_iv);
        titleTv = findViewById(R.id.title_tv);
        turntableIv = findViewById(R.id.turntable_iv);
        albumIv = findViewById(R.id.album_iv);
        stylusIv = findViewById(R.id.stylus_pole_iv);
        phonographGroup = findViewById(R.id.phonograph_group);
        lyricScl = findViewById(R.id.lyric_scl);
        lyricTv = findViewById(R.id.lyric_tv);
        playTimeTv = findViewById(R.id.play_time_tv);
        totalTimeTv = findViewById(R.id.total_time_tv);
        seekBar = findViewById(R.id.seekbar);
        playIv = findViewById(R.id.play_iv);
        //设置顶部toolbar边距
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        toolBar.setPadding(0, statusBarHeight, 0, 0);
        //设置蒙版遮罩层
        int[] maskColors = new int[]{R.color.translucentBlack, R.color.transparent, R.color.translucentBlack};
        GradientDrawable maskGradientDrawable = new DrawableUtil.DrawableBuilder(this).setGradientColors(maskColors).createGradientDrawable();
        mask.setBackgroundDrawable(maskGradientDrawable);
        //设置分割线渐变
        int[] colors = new int[]{R.color.transparent, R.color.white, R.color.transparent};
        GradientDrawable gradientDrawable = new DrawableUtil.DrawableBuilder(this).setGradientOrientation(GradientDrawable.Orientation.LEFT_RIGHT)
                .setGradientColors(colors).setGradientType(GradientDrawable.LINEAR_GRADIENT).createGradientDrawable();
        divide.setBackgroundDrawable(gradientDrawable);
        //设置默认高斯模糊背景
        setCoverAlbum(null);
        //设置着色返回按钮
        Drawable drawable = DrawableUtil.tintDrawable(this, R.drawable.back, R.color.white);
        backIv.setImageDrawable(drawable);
        //动态设置seekBar颜色
        UiUtil.setSeekbarColors(seekBar, Color.RED, Color.RED, Color.GRAY);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //拖动进度条开始拖动的时候调用
                mBinder.seekToPosition(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //拖动进度条停止拖动的时候调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //拖动进度条进度改变的时候调用
            }
        });
        //设置监听器
        addOnClickListeners((View.OnClickListener) view -> {
            int id = view.getId();
            if (id == R.id.back_iv) {
                finish();
            } else if (id == R.id.turntable_iv) {
                AnimatorSet animatorSet = new AnimatorSet();
                Animator fadeoutAnim = getTransitionAnim(phonographGroup, false);
                Animator fadeinAnim = getTransitionAnim(lyricScl, true);
                animatorSet.playTogether(fadeinAnim, fadeoutAnim);
                animatorSet.start();
            } else if (id == R.id.lyric_tv) {
                AnimatorSet animatorSet = new AnimatorSet();
                Animator fadeinAnim = getTransitionAnim(phonographGroup, true);
                Animator fadeoutAnim = getTransitionAnim(lyricScl, false);
                animatorSet.playTogether(fadeinAnim, fadeoutAnim);
                animatorSet.start();
            } else if (id == R.id.backward_iv) {
                playPreviousSong();
            } else if (id == R.id.play_iv) {
                int playStatus = mBinder.getPlayStatus();
                int playItemPosition = mBinder.getPlayItemPosition();
                if (playItemPosition == mPosition) {//外部列表播放和和当前详情是同一首歌
                    switch (playStatus) {
                        case 0://未开始/播放完毕/停止播放
                            String musicUrl = getMusicUrl(playItemPosition);
                            if (TextUtils.isEmpty(musicUrl)) return;
                            mBinder.setPlayItemPosition(mPosition);
                            mBinder.startPlay(musicUrl);
                            updatePlayIcon(false);
                            break;
                        case 1://播放中
                            mBinder.pause();
                            updatePlayIcon(true);
                            break;
                        case 2://暂停
                            mBinder.play();
                            updatePlayIcon(false);
                            break;
                    }
                } else {//当前播放和详情不是同一首，点击开始播放详情这首
                    String musicUrl = getMusicUrl(mPosition);
                    if (TextUtils.isEmpty(musicUrl)) return;
                    mBinder.setPlayItemPosition(mPosition);
                    mBinder.startPlay(musicUrl);
                    updatePlayIcon(false);
                }
            } else if (id == R.id.forward_iv) {
                playNextSong();
            }
        }, R.id.back_iv, R.id.turntable_iv, R.id.lyric_tv, R.id.backward_iv, R.id.play_iv, R.id.forward_iv);
    }

    public Animator getTransitionAnim(View view, boolean isShow) {
        if (null == view) return null;
        float from = isShow ? 0.0f : 1.0f;
        float to = isShow ? 1.0f : 0.0f;
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", from, to);
        alphaAnimator.setDuration(500);
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (isShow) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isShow) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return alphaAnimator;
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        mEntityListBeans = extras.getParcelableArrayList("listBeans");
        mPosition = extras.getInt("position", 0);
        updateSongInfo(mPosition);
    }

    private void updatePlayIcon(boolean isPlaying) {//更新播放按钮
        if (isPlaying) {
            playIv.setImageResource(R.drawable.play);
            startAlbumRotateAnim(false);
            startStylusRotateAnim(false);
        } else {
            playIv.setImageResource(R.drawable.pause);
            startAlbumRotateAnim(true);
            startStylusRotateAnim(true);
        }
    }

    private void bindMusicService() {
        //初始化绑定音乐服务
        Intent intent = new Intent(this, MusicService.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBinder = (MusicBinder) iBinder;
                int playStatus = mBinder.getPlayStatus();
                int playItemPosition = mBinder.getPlayItemPosition();
                if (playItemPosition != mPosition) {//外部列表播放和和当前详情不是同一首歌
                    playIv.setImageResource(R.drawable.play);
                    seekBar.setMax(100);
                    seekBar.setProgress(0);
                    resetAlbumRotateAngle();
                    stylusIv.setRotation(-60);
                    startAlbumRotateAnim(false);
                } else {
                    switch (playStatus) {
                        case 0://未开始/播放完毕/停止播放
                            updatePlayIcon(true);
                            break;
                        case 1://播放中
                            updatePlayIcon(false);
                            break;
                        case 2://暂停
                            updatePlayIcon(true);
                            break;
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unbindMusicService() {
        if (mServiceConnection == null) return;
        unbindService(mServiceConnection);
        mServiceConnection = null;
    }

    private void registerMusicReceiver() {
        if (mReceiver == null) {
            mReceiver = new MusicReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);
                    if (intent == null) return;
                    int currentPosition = intent.getIntExtra("currentPosition", -1);
                    int duration = intent.getIntExtra("duration", -1);
                    int playItemPosition = intent.getIntExtra("playItemPosition", -1);
                    if (playItemPosition != mPosition) {//外部列表播放和和当前详情不是同一首歌
                        playIv.setImageResource(R.drawable.play);
                        seekBar.setMax(100);
                        seekBar.setProgress(0);
                        resetAlbumRotateAngle();
                        stylusIv.setRotation(-60);
                        startAlbumRotateAnim(false);
                    } else {
                        String currentPosition_string = TimeUtil.msToString(currentPosition, TimeUtil.MIN_SEC);
                        if (duration == currentPosition) {//播放完成
                            playIv.setImageResource(R.drawable.play);
                            if (seekBar != null) {
                                seekBar.setMax(100);
                                seekBar.setProgress(0);
                            }
                        } else if (duration > currentPosition) {//播放中
                            if (seekBar != null) {
                                seekBar.setMax(duration);
                                seekBar.setProgress(currentPosition);
                            }
                            if (playTimeTv != null) {
                                playTimeTv.setText(currentPosition_string);
                            }
                        }
                    }
                }
            };
        }
        IntentFilter filter = new IntentFilter(
                "testcomponent.heyongrui.com.componenta.musicservice");
        registerReceiver(mReceiver, filter);
    }

    private void unregisterMusicReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void startStylusRotateAnim(boolean isCloseTo) {//唱针旋转动画
        //设置旋转中心点
        stylusIv.setPivotX(stylusIv.getWidth() * 0.39f);
        stylusIv.setPivotY(0);

        float startAngle = isCloseTo ? -60 : -20;
        float endAngle = isCloseTo ? -20 : -60;
        ObjectAnimator stylusRotationAnim = ObjectAnimator.ofFloat(stylusIv, "rotation", startAngle, endAngle);
        stylusRotationAnim.setDuration(500);
        stylusRotationAnim.setInterpolator(new LinearInterpolator());
        stylusRotationAnim.start();
    }

    private void startAlbumRotateAnim(boolean isStartPlay) {
        if (isStartPlay) {
            float startRotation = albumIv.getRotation();
            ObjectAnimator albumRotationAnim = ObjectAnimator.ofFloat(albumIv, "rotation", startRotation, startRotation + 360f);//添加旋转动画，旋转中心默认为控件中点
            albumRotationAnim.setDuration(4000);//设置动画时间
            albumRotationAnim.setInterpolator(new LinearInterpolator());//动画时间线性渐变
            albumRotationAnim.setRepeatCount(ObjectAnimator.INFINITE);
            albumRotationAnim.setRepeatMode(ObjectAnimator.RESTART);
            //克隆同样的动画给专辑封面view使用
            ObjectAnimator turntableRotationAnim = albumRotationAnim.clone();
            turntableRotationAnim.setTarget(turntableIv);
            if (animatorSet != null && animatorSet.isRunning()) {
                animatorSet.cancel();
            }
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(albumRotationAnim, turntableRotationAnim);
            animatorSet.start();
        } else {
            if (animatorSet != null) {
                animatorSet.cancel();
            }
        }
    }

    private void resetAlbumRotateAngle() {
        albumIv.setRotation(0);
        turntableIv.setRotation(0);
    }

    private void playNextSong() {
        String musicUrl = getMusicUrl(mPosition + 1);
        if (TextUtils.isEmpty(musicUrl)) return;
        if (mBinder == null) return;
        mBinder.stop();
        resetAlbumRotateAngle();
        mBinder.startPlay(musicUrl);
        mBinder.setPlayItemPosition(mPosition + 1);
        updatePlayIcon(false);
        updateSongInfo(mPosition + 1);
        mPosition = mPosition + 1;
    }

    private void playPreviousSong() {
        String musicUrl = getMusicUrl(mPosition - 1);
        if (TextUtils.isEmpty(musicUrl)) return;
        if (mBinder == null) return;
        mBinder.stop();
        resetAlbumRotateAngle();
        mBinder.startPlay(musicUrl);
        mBinder.setPlayItemPosition(mPosition - 1);
        updatePlayIcon(false);
        updateSongInfo(mPosition - 1);
        mPosition = mPosition - 1;
    }

    private String getMusicUrl(int position) {
        if (mEntityListBeans == null || mEntityListBeans.isEmpty()) return null;
        if (position < 0 || position >= mEntityListBeans.size()) return null;
        MonoTabDto.EntityListBean entityListBean = mEntityListBeans.get(position);
        if (entityListBean == null) return null;
        MonoTabDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
        if (meow == null) return null;
        return meow.getMusic_url();
    }

    private void updateSongInfo(int position) {
        if (mEntityListBeans == null || mEntityListBeans.isEmpty()) return;
        if (position < 0 || position >= mEntityListBeans.size()) return;
        MonoTabDto.EntityListBean entityListBean = mEntityListBeans.get(position);
        if (entityListBean == null) return;
        MonoTabDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
        if (meow == null) return;
        //设置歌词
        String lyrics = meow.getLyrics();
        lyrics = TextUtils.isEmpty(lyrics) ? "" : lyrics;
        lyricTv.setText(lyrics);
        //设置专辑封面
        MonoTabDto.EntityListBean.MeowBean.ThumbBean album_cover = meow.getAlbum_cover();
        if (album_cover != null) {
            setCoverAlbum(album_cover.getRaw());
        }
        //设置歌名
        titleTv.setText(meow.getSong_name() + "-" + meow.getArtist());
        //设置音乐时长
        String music_duration = TimeUtil.msToString(meow.getMusic_duration() * 1000, TimeUtil.MIN_SEC);
        totalTimeTv.setText(music_duration);
    }

    private void setCoverAlbum(String coverAlbumUrl) {
        if (TextUtils.isEmpty(coverAlbumUrl)) {
            GlideUtil.loadCircle(this, R.drawable.default_album, albumIv);
            setGaussianBlurBg(null);
        } else {
            GlideUtil.loadCircle(this, coverAlbumUrl, albumIv);
            RequestOptions options = new RequestOptions()
                    .placeholder(testcomponent.heyongrui.com.base.R.drawable.placeholder)
                    .error(testcomponent.heyongrui.com.base.R.drawable.placeholder_fail)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            GlideApp.with(this).asBitmap().load(coverAlbumUrl).apply(options).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    setGaussianBlurBg(resource);
                }
            });
        }
    }

    private void setGaussianBlurBg(Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_album);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Bitmap blurBitmap = ImageUtil.gaussianBlur(this, bitmap, 10, 0.5f);
            if (blurBitmap == null) return;
            bgIv.setImageBitmap(blurBitmap);
        }
    }

    @Override
    protected void onDestroy() {
        unbindMusicService();
        unregisterMusicReceiver();
        super.onDestroy();
    }
}
