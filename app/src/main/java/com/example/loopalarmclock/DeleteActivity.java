package com.example.loopalarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loopalarmclock.adapter.DeleteListViewAdapter;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.database.LoopDatabaseAccess;
import com.example.loopalarmclock.utils.ImmersiveStatusBar;
import com.example.loopalarmclock.utils.ReplaceSkinUtils;
import com.example.loopalarmclock.utils.ToastUtls;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeleteActivity extends Activity implements View.OnClickListener {
    private TextView selectCount, tvDelete, tvSelectAll;
    private ImageView ivBack;
    private ListView lvDelete;
    private List<LoopAlarmBean> data;
    private DeleteListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
        //沉浸式状态栏
        Window window=getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        selectCount = (TextView) findViewById(R.id.select_count);
        ivBack = (ImageView) findViewById(R.id.iv_back_delete);
        lvDelete = (ListView) findViewById(R.id.delete_lv);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvSelectAll = (TextView) findViewById(R.id.tv_selectall);

        tvDelete.setOnClickListener(this);
        tvSelectAll.setOnClickListener(this);
        data = LoopDatabaseAccess.queryAllData("Loop");
        adapter = new DeleteListViewAdapter(this, data);
        lvDelete.setAdapter(adapter);
        ivBack.setOnClickListener(this);


    }
    private Thread computeCount(){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0x111);
                }
            }
        });
        return thread;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启线程随时计算选中个数并刷新选中多少项的提醒
        computeCount().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        computeCount().interrupt();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x111) {
                if (adapter.getCount()>0) {

                    if (adapter.getNumber()<0){
                        selectCount.setText("选中" +0+ "项");
                    }else {
                        selectCount.setText("选中" +adapter.getNumber()+ "项");
                    }

                }else {
                    computeCount().interrupt();
                    ToastUtls.showToast(DeleteActivity.this,"主人,闹钟已经被清楚干净啦！");
                    finish();
                }
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                adapter.deleteData();
                break;
            case R.id.tv_selectall:
                if (adapter.checkCheckedAllItem()) {
                    adapter.selectAllOrNone(false);
                } else {
                    adapter.selectAllOrNone(true);
                }
                break;
            case R.id.iv_back_delete:
                for (int i = 0; i < data.size(); i++) {
                    LoopAlarmBean bean = data.get(i);
                    bean.setSelected(false);
                }
                finish();
                break;
        }
    }
}
