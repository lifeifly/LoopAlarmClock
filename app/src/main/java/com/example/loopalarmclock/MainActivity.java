package com.example.loopalarmclock;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.loopalarmclock.adapter.AlarmRecyclerViewAdapter;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.database.LoopDatabaseAccess;
import com.example.loopalarmclock.loop.LoopAlarmAcitivity;
import com.example.loopalarmclock.normal.NormalAlarmActivity;

import com.example.loopalarmclock.utils.ImmersiveStatusBar;
import com.example.loopalarmclock.utils.ReplaceSkinUtils;

import java.util.List;

public class MainActivity extends Activity {
    private TabHost tabHost;
    private AlarmLinearLayout alarmLinearLayout;
    public static List<LoopAlarmBean> data;
    public AlarmRecyclerViewAdapter adapter;
    private TextView tvHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
        //沉浸式状态栏
        Window window = getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        setTab();
        alarmLinearLayout = (AlarmLinearLayout) findViewById(R.id.alarm_tab);
        tvHint=(TextView)findViewById(R.id.tv_hint);

    }


    @Override
    protected void onResume() {
        super.onResume();
        data = LoopDatabaseAccess.queryAllData("Loop");
        if (data.size()==0){
            tvHint.setVisibility(View.VISIBLE);
        }else {
            tvHint.setVisibility(View.GONE);
        }
        //设置适配器
        adapter = new AlarmRecyclerViewAdapter(MainActivity.this, data);
        alarmLinearLayout.rv_alarm.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlarmRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = null;
                LoopAlarmBean bean = data.get(position);
                if (bean.getType().equals("loop")) {
                    intent = new Intent(MainActivity.this, LoopAlarmAcitivity.class);

                } else if (bean.getType().equals("normal")) {
                    intent = new Intent(MainActivity.this, NormalAlarmActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void OnItemLongClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });
        //设置布局管理器
        alarmLinearLayout.rv_alarm.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    //设置tab
    private void setTab() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabView tabView1 = new TabView(this, "闹钟", R.drawable.tab_clock_selector);
        TabView tabView2 = new TabView(this, "我的", R.drawable.tab_settings_selector);
        //去除分割线
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(tabView1.getView()).setContent(R.id.alarm_tab));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(tabView2.getView()).setContent(R.id.settings_tab));
        //设置默认选择0号标签
        tabHost.setCurrentTab(0);
    }


    //代表每个tabwidget的view
    class TabView {
        private View view;
        private Context context;
        private TextView tv_tab;
        private ImageView iv_tab;
        private String tv;
        private int imageRes;

        public TabView(Context context, String tv, int iamgeRes) {
            this.context = context;
            this.tv = tv;
            this.imageRes = iamgeRes;
        }

        public View getView() {
            if (view == null) {
                this.view = LayoutInflater.from(context).inflate(R.layout.tab_view_layout, null);
                tv_tab = view.findViewById(R.id.tv_tab);
                iv_tab = view.findViewById(R.id.iv_tab);
                tv_tab.setText(tv);
                iv_tab.setImageResource(imageRes);
            }
            return view;
        }
    }


}
