package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import testcomponent.heyongrui.com.base.base.BaseFragment;
import testcomponent.heyongrui.com.base.widget.catloadingview.CatLoadingView;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.widget.x5webview.X5WebChromeClient;
import testcomponent.heyongrui.com.componenta.widget.x5webview.X5WebView;

/**
 * Created by lambert on 2018/11/6.
 */

public class X5WebViewFragment extends BaseFragment {

    private ImageView backIv;
    private ImageView finishIv;
    private TextView titleTv;
    private ProgressBar progressBar;
    private X5WebView x5WebView;
    private ImageView forwardIv;
    private Dialog progressDialog;

    public static X5WebViewFragment newInstance(String homeUrl, String htmlData, boolean isShowDialog) {
        X5WebViewFragment fragment = new X5WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("homeUrl", homeUrl);
        bundle.putString("htmlData", htmlData);
        bundle.putBoolean("isShowDialog", isShowDialog);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_x5_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initX5WebView(x5WebView);
        getData();
    }

    private void initView() {
        backIv = mView.findViewById(R.id.back_iv);
        finishIv = mView.findViewById(R.id.finish_iv);
        forwardIv = mView.findViewById(R.id.forward_iv);
        titleTv = mView.findViewById(R.id.title_tv);
        progressBar = mView.findViewById(R.id.progressbar);
        setProgressBarColors(progressBar, ContextCompat.getColor(mContext, R.color.gray), ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        x5WebView = mView.findViewById(R.id.x5webview);
        addOnClickListeners(view -> {
            int id = view.getId();
            if (id == R.id.back_iv) {
                if (x5WebView.canGoBack()) {
                    finishIv.setVisibility(View.VISIBLE);
                    x5WebView.goBack();
                    finishIv.setVisibility(x5WebView.canGoBack() ? View.VISIBLE : View.GONE);
                } else {
                    getActivity().onBackPressed();
                }
            } else if (id == R.id.finish_iv) {
                getActivity().finish();
            } else if (id == R.id.forward_iv) {
                if (x5WebView.canGoForward()) {
                    x5WebView.goForward();
                }
                forwardIv.setVisibility(x5WebView.canGoForward() ? View.VISIBLE : View.GONE);
            }
        }, R.id.back_iv, R.id.finish_iv, R.id.forward_iv);
    }

    public void setProgressBarColors(ProgressBar progressBar, int backgroundColor, int progressColor) {
        //Background
        ClipDrawable bgClipDrawable = new ClipDrawable(new ColorDrawable(backgroundColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        bgClipDrawable.setLevel(10000);
        //Progress
        ClipDrawable progressClip = new ClipDrawable(new ColorDrawable(progressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        //Setup LayerDrawable and assign to progressBar
        Drawable[] progressDrawables = {bgClipDrawable, progressClip/*second*/, progressClip};
        LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);
        progressLayerDrawable.setId(0, android.R.id.background);
        progressLayerDrawable.setId(1, android.R.id.secondaryProgress);
        progressLayerDrawable.setId(2, android.R.id.progress);
        progressBar.setProgressDrawable(progressLayerDrawable);
    }

    private void initX5WebView(X5WebView x5WebView) {
        //隐藏滚动块
        IX5WebViewExtension ex = x5WebView.getX5WebViewExtension();
        if (ex != null) {//这里要判空，否则如果是调用手机浏览器内核的话会报空
            ex.setScrollBarFadingEnabled(false);
        }
        x5WebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switchProgressDialog(false);
            }
        });
        X5WebChromeClient webChromeClient = new X5WebChromeClient(getActivity()) {

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                //设置进度条和标题
                if (progressBar != null) {
                    progressBar.setProgress(i);
                    if (progressBar != null && i != 100) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else if (progressBar != null) {
                        titleTv.setText(webView.getTitle());
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReachedMaxAppCacheSize(long requiredStorage, long l1, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(requiredStorage * 2);
            }
        };
        x5WebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        x5WebView.setWebChromeClient(webChromeClient);
    }

    private void getData() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        String homeUrl = arguments.getString("homeUrl", "");
        String htmlData = arguments.getString("htmlData", "");
        boolean isShowDialog = arguments.getBoolean("isShowDialog", true);
        if (!TextUtils.isEmpty(homeUrl)) {//加载网页
            x5WebView.loadUrl(homeUrl);
        } else if (!TextUtils.isEmpty(htmlData)) {//加载HTML代码
            x5WebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);//这种写法可以正确解码
        }
        switchProgressDialog(isShowDialog);
    }

    private void switchProgressDialog(boolean isShow) {
        if (isShow) {
            if (progressDialog == null) {
                progressDialog = new CatLoadingView(mContext);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
