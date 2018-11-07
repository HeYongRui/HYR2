package testcomponent.heyongrui.com.componenta.ui.mono.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
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
    TextView headTv;
    TextView summaryTv;
    private MonoAdapter monoAdapter;

    public static void launchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MonoTeaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
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
        headTv = headView.findViewById(R.id.head_tv);
        summaryTv = headView.findViewById(R.id.summary_tv);
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
            MonoTeaDto.EntityListBean entityListBean = ((MonoAdapter) adapter).getItem(position).getEntityListBean();
            if (entityListBean == null) return;
            MonoTeaDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
            if (meow == null) return;
            String rec_url = meow.getRec_url();
            if (TextUtils.isEmpty(rec_url)) return;
            MonoH5Activity.launchActivity(this, rec_url);
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
                        //根据时间状态和内容更新头布局
                        StringBuffer stringBuffer = new StringBuffer();
                        if (TimeUtil.isPM()) {
                            if (afternoonTea == null) {
                                afternoonTea = morningTea;
                                stringBuffer.append("早茶");
                            } else {
                                stringBuffer.append("下午茶");
                            }
                        } else {
                            stringBuffer.append("早茶");
                        }
                        stringBuffer.append(TimeUtil.getDateString(new Date(), TimeUtil.DAY_TWO));
                        headTv.setText(stringBuffer);
                        //添加内容列表
                        addData(TimeUtil.isPM() ? afternoonTea : morningTea);
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {
                        ToastUtils.showShort(errorMsg);
                    }
                }));
    }

    private void addData(MonoTeaDto.TeaBean teaBean) {
        if (teaBean == null) return;
        List<MonoTeaDto.EntityListBean> entityListBeans = teaBean.getEntity_list();
        if (entityListBeans == null || entityListBeans.isEmpty()) return;
        //设置头部概要信息
        StringBuffer stringBuffer = new StringBuffer();
        String[] stringArray = getResources().getStringArray(R.array.weekdays);
        stringBuffer.append(stringArray[TimeUtil.getWeek(new Date())]);
        stringBuffer.append("/" + entityListBeans.size() + "篇内容，");
        stringBuffer.append(teaBean.getRead_time());
        summaryTv.setText(stringBuffer);
        //添加列表数据
        for (MonoTeaDto.EntityListBean entityListBean : entityListBeans) {
            MonoTeaDto.EntityListBean.MeowBean meow = entityListBean.getMeow();
            if (meow == null) continue;
            List<MonoTeaDto.EntityListBean.MeowBean.ThumbBean> pics = meow.getPics();
            if (pics == null || pics.isEmpty()) {
                monoAdapter.addData(new MonoMultipleItem(MonoMultipleItem.TYPE_ONE, entityListBean));
            } else {
                monoAdapter.addData(new MonoMultipleItem(MonoMultipleItem.TYPE_TWO, entityListBean));
            }
        }
    }
}
