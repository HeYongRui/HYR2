package com.heyongrui.testcomponent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Random;

import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.widget.catloadingview.CatLoadingView;
import testcomponent.heyongrui.com.base.widget.numberruntextview.EditTextWithClear;
import testcomponent.heyongrui.com.base.widget.numberruntextview.NumberRunningTextView;
import testcomponent.heyongrui.com.base.widget.tickerview.TickerUtils;
import testcomponent.heyongrui.com.base.widget.tickerview.TickerView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv;
    private TickerView tickerView;
    private NumberRunningTextView runTv;
    private int tickerCount;
    private ImageView iv;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tickerView = findViewById(R.id.tv_ticker);
        runTv = findViewById(R.id.run_tv);
        EditText editText = findViewById(R.id.edit);
        editText.addTextChangedListener(new EditTextWithClear(editText, "-".charAt(0)));
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        addOnClickListeners(this, R.id.tv_ticker, R.id.run_tv, R.id.tv, R.id.btn1, R.id.btn2, R.id.iv);
    }

    private String generateChars(Random random, String list, int numDigits) {
        final char[] result = new char[numDigits];
        for (int i = 0; i < numDigits; i++) {
            result[i] = list.charAt(random.nextInt(list.length()));
        }
        return new String(result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ticker:
                if (tickerCount < 5) {
                    tickerView.setCharacterLists(TickerUtils.provideNumberList());
                    tickerView.setText("￥" + new Random().nextInt(500) + "." + new Random().nextInt(100));
                } else if (tickerCount < 10 && tickerCount >= 5) {
                    tickerView.setCharacterLists(TickerUtils.provideAlphabeticalList());
                    int digits = new Random().nextInt(2) + 6;
                    tickerView.setText(generateChars(new Random(), TickerUtils.provideAlphabeticalList(), digits));
                }
                if (tickerCount == 10) {
                    tickerCount = 0;
                } else {
                    tickerCount++;
                }
                break;
            case R.id.run_tv:
                runTv.setContent(new Random().nextInt(500) + ".47");
                break;
            case R.id.btn1:
                //自动注册方式实现登录拦截
                CC.obtainBuilder("ComponentA")
                        .setActionName("LoginInterceptor")
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC loginCC, CCResult result) {
                                if (result.isSuccess()) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("result", result.toString());
                                    map.put("bundle", bundle);
                                    map.put("loginCC", loginCC.toString());
                                    CC.obtainBuilder("ComponentA")
                                            .setActionName("ComponentActivityA")
                                            .addParams(map)
                                            .build().callAsync();
                                }
                                Log.i("MainActivity", result.toString() + "\n" + loginCC.toString());
                            }
                        });
                break;
            case R.id.btn2:
                CC.obtainBuilder("ComponentA")
                        .setActionName("openUnsplash")
                        .build()
                        .call();
                break;
            case R.id.iv:
                CatLoadingView catLoadingView = new CatLoadingView(this);
                catLoadingView.show();
                RequestOptions options = new RequestOptions();
                options.placeholder(R.drawable.ic_launcher_background);
                options.diskCacheStrategy(DiskCacheStrategy.NONE);
                options.skipMemoryCache(true);
//                Glide.with(this).load("http://img1.dzwww.com:8080/tupian_pl/20150813/16/7858995348613407436.jpg").into(iv);
                Glide.with(this).load("https://source.unsplash.com/random/1080x1920").apply(options).into(iv);
                break;
        }
    }
}
