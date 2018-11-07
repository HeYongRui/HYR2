package testcomponent.heyongrui.com.componenta.ui.unsplash.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.regex.Pattern;

import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.config.glide.GlideApp;
import testcomponent.heyongrui.com.base.config.glide.OnProgressListener;
import testcomponent.heyongrui.com.base.util.DrawableUtil;
import testcomponent.heyongrui.com.base.util.GlideUtil;
import testcomponent.heyongrui.com.base.widget.CircleProgressView;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;
import testcomponent.heyongrui.com.componenta.widget.PinchImageView;

/**
 * Created by lambert on 2018/10/25.
 */

public class UnsplashDetailActivity extends BaseActivity implements View.OnClickListener {

    private PinchImageView imageView;
    private CircleProgressView circleProgressView;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private int targetTop;
    private int targetLeft;
    private int targetWidth;
    private int targetHeight;
    private ImageView authorIv;
    private TextView authorTv;
    private ImageView downloadIv;
    private String full_url;

    public static void launchActivity(Context context, UnsplashPicDto unsplashPicDto, int targetTop, int targetLeft, int targetWidth, int targetHeight) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("unsplashPicDto", unsplashPicDto);
        bundle.putInt("top", targetTop);
        bundle.putInt("left", targetLeft);
        bundle.putInt("width", targetWidth);
        bundle.putInt("height", targetHeight);
        intent.putExtras(bundle);
        intent.setClass(context, UnsplashDetailActivity.class);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_unsplash_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
        initData();
        initEnterAnim(savedInstanceState);
    }

    private void initView() {
        imageView = findViewById(R.id.iv);
        authorIv = findViewById(R.id.author_iv);
        authorTv = findViewById(R.id.author_tv);
        downloadIv = findViewById(R.id.download_iv);
        GradientDrawable gradientDrawable = new DrawableUtil.DrawableBuilder(this).setGradientRoundRadius(10).setColor(R.color.gray).createGradientDrawable();
        downloadIv.setBackgroundDrawable(gradientDrawable);
//        UiUtil.setOnclickFeedBack(this, R.color.white, R.color.gray, downloadIv);
        circleProgressView = findViewById(R.id.progressView);
        circleProgressView.setMax(100);
        addOnClickListeners(this, R.id.iv, R.id.download_iv);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv) {
            onBackPressed();
        } else if (id == R.id.download_iv) {
            startDownload(full_url);
        }
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        //过场动画所需参数(上个页面小图的具体位置和宽高)
        targetTop = extras.getInt("top");
        targetLeft = extras.getInt("left");
        targetWidth = extras.getInt("width");
        targetHeight = extras.getInt("height");
        //异步加载大图
        UnsplashPicDto unsplashPicDto = extras.getParcelable("unsplashPicDto");
        if (unsplashPicDto == null) return;
        UnsplashPicDto.UserBean user = unsplashPicDto.getUser();
        if (user != null) {
            //设置作者名字
            String name = user.getName();
            if (!TextUtils.isEmpty(name)) {
                authorTv.setText(name);
            }
            UnsplashPicDto.UserBean.ProfileImageBean profile_image = user.getProfile_image();
            if (profile_image != null) {//设置作者头像
                String large = profile_image.getLarge();
                GlideUtil.loadCircle(this, large, authorIv);
            }
        }
        UnsplashPicDto.UrlsBean urls = unsplashPicDto.getUrls();
        if (urls != null) {//设置大图
            full_url = urls.getFull();
            if (TextUtils.isEmpty(full_url)) return;
            String fileName = extractFileName(full_url);//提取文件名
            File saveFile = getSaveFile(fileName);//判断文件是否已存在
            OnProgressListener onProgressListener = (isComplete, percentage, bytesRead, totalBytes) -> {
                if (isComplete) {
                    circleProgressView.setVisibility(View.GONE);
                } else {
                    circleProgressView.setVisibility(View.VISIBLE);
                    circleProgressView.setProgress(percentage);
                }
            };
            if (saveFile.exists()) {
                GlideUtil.loadWithProgress(saveFile, imageView, onProgressListener);
            } else {
                GlideUtil.loadWithProgress(full_url, imageView, onProgressListener);
            }
        }
    }

    private void initEnterAnim(Bundle savedInstanceState) {//初始化进场动画
        if (savedInstanceState == null) {
            ViewTreeObserver observer = imageView.getViewTreeObserver();
            //此方法在视图绘制前会被调用，测量结束，客户获取到一些数据。再计算一些动态宽高时可以使用
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //调用一次后需要注销这个监听，否则会阻塞ui线程
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);
                    mLeftDelta = targetLeft - screenLocation[0];
                    mTopDelta = targetTop - screenLocation[1];
                    mWidthScale = (float) targetWidth / imageView.getWidth();
                    mHeightScale = (float) targetHeight / imageView.getHeight();
                    enterAnimation(() -> {
                        Matrix matrix = imageView.getMatrix();
                        imageView.setImageMatrix(matrix);
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    });
                    return true;
                }
            });
        }
    }

    private void enterAnimation(Runnable enterAction) {
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mWidthScale);
        imageView.setScaleY(mHeightScale);
        imageView.setTranslationX(mLeftDelta);
        imageView.setTranslationY(mTopDelta);
        TimeInterpolator sDecelerator = new DecelerateInterpolator();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.animate().setDuration(150).scaleX(1).scaleY(1).
                    translationX(0).translationY(0).setInterpolator(sDecelerator).withEndAction(enterAction);
        }
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.window_background));
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(150);
        bgAnim.start();
    }

    public void exitAnimation(Runnable endAction) {
        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.animate().setDuration(150).scaleX(mWidthScale).scaleY(mHeightScale).
                    translationX(mLeftDelta).translationY(mTopDelta).setInterpolator(sInterpolator).withEndAction(endAction);
        }
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.window_background));
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(150);
        bgAnim.start();
    }

    private void startDownload(String download_url) {
        if (TextUtils.isEmpty(download_url)) return;
        //提取文件名
        String fileName = extractFileName(download_url);
        //判断文件是否已存在
        File saveFile = getSaveFile(fileName);
        if (saveFile.exists()) {
            ToastUtils.showShort("文件已保存");
            return;
        }
        //检查权限
        checkPermission(saveFile, download_url);
    }

    private String extractFileName(String download_url) {//提取文件名
        if (TextUtils.isEmpty(download_url)) return "";
        String fileName = URLUtil.guessFileName(download_url, null, null);
        String[] split = fileName.split(Pattern.quote("."));
        if (split != null && split.length > 1) {
            String endChar = split[split.length - 1];
            if (!endChar.equalsIgnoreCase("jpg") && !endChar.equalsIgnoreCase("png")
                    && !endChar.equalsIgnoreCase("gif")) {
                fileName = split[0] + ".jpg";
            }
        } else {
            fileName = fileName + ".jpg";
        }
        return fileName;
    }

    private File getSaveFile(String fileName) {//获取保存文件
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/Download";
        File downloadFolderFile = new File(path);
        if (!downloadFolderFile.exists()) {
            downloadFolderFile.mkdir();
        }
        File savaFile = new File(downloadFolderFile.getPath(), fileName);
        return savaFile;
    }

    private void checkPermission(File saveFile, String download_url) {//权限检查
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        doDownload(saveFile, download_url);
                    } else {
                        ToastUtils.showShort("请打开文件操作权限");
                    }
                });

    }

    private void doDownload(File savaFile, String download_url) {//执行下载
        GlideApp.with(this).downloadOnly().load(download_url).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                ToastUtils.showShort("保存失败");
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    //FileInputStream fis = new FileInputStream(resource);
                    //Bitmap bmp = BitmapFactory.decodeStream(fis);
                    boolean isSuccess = FileUtils.copyFile(resource, savaFile);
                    if (isSuccess) {
                        ToastUtils.showShort("文件保存" + savaFile.getPath());
                    }
                } catch (Exception e) {
                    ToastUtils.showShort("保存失败");
                    e.printStackTrace();
                }
                return false;
            }
        }).preload();
    }

    @Override
    public void onBackPressed() {
        exitAnimation(() -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }
}
