package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.componenta.R;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoH5Activity extends BaseActivity {

    public static void launchActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setClass(context, MonoH5Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mono_h5;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initFragment();
    }

    private void initFragment() {
        Bundle extras = getIntent().getExtras();
        String url = null;
        if (extras != null) {
            url = extras.getString("url", "");
        }
        X5WebViewFragment x5WebViewFragment = X5WebViewFragment.newInstance(url, null, true);
        replaceFragment(R.id.x5webview_content_fragment, x5WebViewFragment, "");
    }
}
