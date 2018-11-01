package testcomponent.heyongrui.com.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testcomponent.heyongrui.com.base.Global;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoginSimulation;
    TextView tvContent;

    private String callId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLoginSimulation = findViewById(R.id.btn_login_simulation);
        tvContent = findViewById(R.id.tv_content);
        btnLoginSimulation.setOnClickListener(this);
        callId = CCUtil.getNavigateCallId(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login_simulation) {
            Global.loginUser = new Global.User(1, "Mr.He2");
            CC.obtainBuilder("ComponentB")
                    .setActionName("setUserInfo")
                    .addParam(Global.KEY_USER, Global.loginUser)
                    .build()
                    .callAsync();
            finish();
        }
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
