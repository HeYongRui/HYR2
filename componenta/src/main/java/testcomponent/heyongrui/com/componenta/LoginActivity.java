package testcomponent.heyongrui.com.componenta;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testcomponent.heyongrui.com.base.Global;

/**
 * Created by lambert on 2018/9/30.
 */

public class LoginActivity extends AppCompatActivity {

    private String callId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);
        textView.setTextColor(Color.WHITE);
        textView.setText("登录界面");
        callId = CCUtil.getNavigateCallId(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textView.getText().toString().trim();
                if (!TextUtils.isEmpty(username)) {
                    Global.loginUser = new Global.User(1, username);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //判断是否为CC调用打开本页面
        if (callId != null) {
            CCResult result;
            if (Global.loginUser == null) {
                result = CCResult.error("login canceled");
            } else {
                //演示跨app传递自定义类型及各种集合类型
                List<Global.User> list = new ArrayList<>();
                list.add(new Global.User(1, "aaa"));
                list.add(new Global.User(3, "ccc"));
                SparseArray<Global.User> userSparseArray = new SparseArray<>();
                userSparseArray.put(1, new Global.User(1, "a"));
                userSparseArray.put(10, new Global.User(10, "a"));
                userSparseArray.put(30, new Global.User(30, "a"));
                Global.User[][] userArray = new Global.User[5][2];
                SparseIntArray sparseIntArray = new SparseIntArray();
                sparseIntArray.put(1, 111);
                sparseIntArray.put(2, 222);
                Map<String, Global.User> map = new HashMap<>();
                map.put("user1", new Global.User(1, "111"));
                map.put("user2", new Global.User(2, "222"));

                result = CCResult.success(Global.KEY_USER, Global.loginUser) //User
                        .addData("list", list) // List<User>
                        .addData("nullObject", null) //null
                        .addData("sparseArray", userSparseArray) //SparseArray<User>
                        .addData("sparseIntArray", sparseIntArray) //SparseIntArray/SparseLongArray
                        .addData("user2Array", userArray) // User[][]
                        .addData("untypedArray", list.toArray()) // Object[]
                        .addData("typedArray", list.toArray(new Global.User[]{})) // User[]
                        .addData("map", map) // Map
                ;
            }
            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
            CC.sendCCResult(callId, result);
        }
    }
}
