package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.service.MonoSerevice;

/**
 * Created by lambert on 2018/11/21.
 */

public class MonoCategoryDetailActivity extends BaseActivity {

    private MonoSerevice monoSerevice;

    public static void launchActivity(Context context, int meow_id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("meow_id", meow_id);
        intent.putExtras(bundle);
        intent.setClass(context, MonoCategoryDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mono_category_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        int meow_id = extras.getInt("meow_id", -1);
        if (meow_id == -1) return;
        //直接拼接H5网页打开
//        String meowH5Url = String.format("http://mmmono.com/g/meow/%d/", meow_id);
//        X5WebViewFragment x5WebViewFragment = X5WebViewFragment.newInstance(meowH5Url, null,false);
//        replaceFragment(R.id.x5webview_content_fragment, x5WebViewFragment, "");
        //通过接口获取HTML内容再解析展示
        getMeowDetail(meow_id);
    }

    private void getMeowDetail(int meow_id) {
        monoSerevice = monoSerevice == null ? new MonoSerevice() : monoSerevice;
        mRxManager.add(monoSerevice.getMeowDetail(meow_id)
                .subscribeWith(new ResponseDisposable<ResponseBody>(this) {
                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String xmlContent = responseBody.string();
                            X5WebViewFragment x5WebViewFragment = X5WebViewFragment.newInstance(null, xmlContent, false);
                            replaceFragment(R.id.x5webview_content_fragment, x5WebViewFragment, "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }
}
