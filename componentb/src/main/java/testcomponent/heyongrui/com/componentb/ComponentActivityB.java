package testcomponent.heyongrui.com.componentb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.HashMap;

import testcomponent.heyongrui.com.base.JsonFormat;

/**
 * Created by lambert on 2018/10/8.
 */

public class ComponentActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setContentView(textView);
        textView.setGravity(Gravity.CENTER);
        textView.setText("ComponentActivityB");

        Serializable parms = getIntent().getSerializableExtra("parms");
        StringBuffer stringBuffer = new StringBuffer();
        if (parms != null) {
            if (parms instanceof HashMap) {
                Object bundle = ((HashMap) parms).get("bundle");
                if (bundle instanceof Bundle) {
                    String test = ((Bundle) bundle).getString("result");
                    test = "\n" + JsonFormat.format(test);
                    stringBuffer.append(test);
                    textView.setText(test);
                    Logger.i(test);
                }
                Object key = ((HashMap) parms).get("loginCC");
                if (key instanceof String) {
                    String loginCC = "\n" + JsonFormat.format((String) key);
                    stringBuffer.append(loginCC);
                    Logger.i(loginCC);
                }
            }
        }
        if (!TextUtils.isEmpty(stringBuffer.toString())) {
            textView.setText(stringBuffer);
        }
    }
}