package com.example.loopalarmclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.recyclerview.widget.RecyclerView;

import com.example.loopalarmclock.loop.LoopAlarmAcitivity;
import com.example.loopalarmclock.normal.NormalAlarmActivity;
import com.example.loopalarmclock.R;

public class AlarmLinearLayout extends LinearLayout {
    public RecyclerView rv_alarm;
    public Button btn_add_alarm;
    public Context context;


    public AlarmLinearLayout(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public AlarmLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public AlarmLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    //加载完成时调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rv_alarm = (RecyclerView) findViewById(R.id.alarm_rv);
        btn_add_alarm = (Button) findViewById(R.id.btn_addalarm);

        btn_add_alarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //加载对话框view
                final View view = View.inflate(context, R.layout.add_alarm_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        btn_add_alarm.setRotation(270);
                    }
                })
                        .setView(view)
                        .create();
                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(R.drawable.addalarm_shape);
                dialog.show();
                //初始化对话框布局
                LinearLayout llNormal = (LinearLayout) view.findViewById(R.id.ll_nomrmal);
                LinearLayout llLoop = (LinearLayout) view.findViewById(R.id.ll_loop);
                //设置view的点击跳转事件
                llNormal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NormalAlarmActivity.class);
                        context.startActivity(intent);
                        btn_add_alarm.setRotation(270);
                        dialog.dismiss();
                    }
                });
                llLoop.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LoopAlarmAcitivity.class);
                        context.startActivity(intent);
                        btn_add_alarm.setRotation(270);

                        dialog.dismiss();
                    }
                });
                btn_add_alarm.setRotation(45);

            }
        });
    }
}
