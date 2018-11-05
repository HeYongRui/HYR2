package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.base.util.TimeUtil;
import testcomponent.heyongrui.com.base.widget.itemdecoration.RecycleViewItemDecoration;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;
import testcomponent.heyongrui.com.componenta.net.service.MonoSerevice;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoAdapter;
import testcomponent.heyongrui.com.componenta.ui.mono.adapter.MonoMultipleItem;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoTeaActivity extends BaseActivity {

    RecyclerView recyclerView;
    private MonoAdapter monoAdapter;

    public static void launchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MonoTeaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_mono_tea;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
        getTea();
    }

    private void initRecyclerView() {
        List<MonoMultipleItem> data = new ArrayList<>();
        monoAdapter = new MonoAdapter(data);
        View headView = getLayoutInflater().inflate(R.layout.header_monotea, null);
        TextView headTv = headView.findViewById(R.id.head_tv);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/hanyizhuzi.ttf");
        headTv.setTypeface(typeFace);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(TimeUtil.isPM() ? "下午茶" : "早茶");
        stringBuffer.append(TimeUtil.getDateString(new Date(), TimeUtil.DAY_TWO));
        headTv.setText(stringBuffer);
        monoAdapter.setHeaderView(headView);
        monoAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        monoAdapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewItemDecoration(this, 1));
        monoAdapter.setOnItemClickListener((adapter, view, position) -> {
        });
    }


    private void getTea() {
        mRxManager.add(new MonoSerevice().getTea()
                .subscribeWith(new ResponseDisposable<MonoTeaDto>(this) {
                    @Override
                    protected void onSuccess(MonoTeaDto monoTeaDto) {
                        if (monoTeaDto == null) return;
                        MonoTeaDto.TeaBean morningTea = monoTeaDto.getMorning_tea();
                        MonoTeaDto.TeaBean afternoonTea = monoTeaDto.getAfternoon_tea();
                        if (TimeUtil.isPM()) {
                            if (afternoonTea != null) {
                                addData(afternoonTea.getEntity_list());
                            }
                        } else {
                            if (morningTea != null) {
                                addData(morningTea.getEntity_list());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {

                    }
                }));
    }

    private void addData(List<MonoTeaDto.EntityListBean> entityListBeans) {
        if (entityListBeans == null || entityListBeans.isEmpty()) return;
        for (MonoTeaDto.EntityListBean entityListBean : entityListBeans) {
            monoAdapter.addData(new MonoMultipleItem(MonoMultipleItem.TYPE_ONE, MonoMultipleItem.IMG_SPAN_SIZE, entityListBean));
        }
    }
}
