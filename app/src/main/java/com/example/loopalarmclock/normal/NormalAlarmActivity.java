package com.example.loopalarmclock.normal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.loopalarmclock.R;

import com.example.loopalarmclock.bean.Constants;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.database.LoopDatabaseAccess;

import com.example.loopalarmclock.utils.ImmersiveStatusBar;

import com.example.loopalarmclock.utils.ReplaceSkinUtils;
import com.example.loopalarmclock.utils.ToastUtls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NormalAlarmActivity extends Activity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
    //组件
    private TimePicker timePicker;
    private ImageView ivBack;
    private TextView tvInterval, tvTime, tvLabel, tvVabrate, tvWeek, tvSave;
    private RelativeLayout rlInterval, rlTime, rlLabel, rlVabrate;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox0;
    //需要的数据
    private String type = "normal";
    private int hour, minute;
    private int interval = 10, time = 5, times = 3;
    private int vabrate = 1;
    private String label = "标签";
    private String week;
    //1开启，0关闭
    private int enable = 1;
    private int tag = 0;
    //对话框组件
    private TextView tvIntervalNumber, tvTimesNumber;
    private SeekBar sbInterval, sbTimes;
    private Button btnCancel, btnSure;
    private AlertDialog timeDialog;
    //开启的周期list
    List<String> weekList;
    private LoopAlarmBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_alarm);
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
        //沉浸式状态栏
        Window window=getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        weekList = new ArrayList<>();
        initView();
        //如果是从列表跳转来的
        if (getIntent().getExtras() != null) {
            bean = (LoopAlarmBean) getIntent().getExtras().getSerializable("bean");
            timePicker.setCurrentHour(bean.getHour());
            timePicker.setCurrentMinute(bean.getMinute());
            tvInterval.setText(bean.getInterval() + "分钟," + bean.getTimes() + "次");
            tvTime.setText(bean.getTime() + "分钟");
            tvLabel.setText(bean.getLabel());
            tvVabrate.setText(bean.getVabrate() == 0 ? "关" : "开");

            enable = bean.getEnable();
            hour = bean.getHour();
            minute = bean.getMinute();
            tag = bean.getTag();
            vabrate = bean.getVabrate();
            time = bean.getTime();
            times = bean.getTimes();
            interval = bean.getInterval();

            String[] selectedWeek = bean.getWeek().split(",");
            if (selectedWeek.length == 7) {
                checkBox0.setChecked(true);
                tvWeek.setText("每天");
            } else if (selectedWeek.length > 0 && selectedWeek.length < 7) {
                for (String s : selectedWeek) {
                    switch (s) {
                        case "周一":
                            checkBox1.setChecked(true);
                            weekList.add("周一");
                            break;
                        case "周二":
                            checkBox2.setChecked(true);
                            weekList.add("周二");
                            break;
                        case "周三":
                            checkBox3.setChecked(true);
                            weekList.add("周三");
                            break;
                        case "周四":
                            checkBox4.setChecked(true);
                            weekList.add("周四");
                            break;
                        case "周五":
                            checkBox5.setChecked(true);
                            weekList.add("周五");
                            break;
                        case "周六":
                            checkBox6.setChecked(true);
                            weekList.add("周六");
                            break;
                        case "周日":
                            checkBox7.setChecked(true);
                            weekList.add("周日");
                            break;
                    }
                }
                tvWeek.setText(bean.getWeek());
            } else {
                tvWeek.setText("不重复");
            }
        } else {
            //从添加按钮跳转，提供默认参数
            //默认每天
            checkBox0.setChecked(true);
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
            tvWeek.setText("每天");
            setTime();
        }
    }

    private void setTime() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minu) {
                hour = hourOfDay;
                minute = minu;
            }
        });
    }

    private void initView() {
        timePicker = (TimePicker) findViewById(R.id.timer_picker);
        timePicker.setIs24HourView(true);
        tvSave = (TextView) findViewById(R.id.btn_save_normal);
        ivBack = (ImageView) findViewById(R.id.iv_back_titlebar);
        tvInterval = (TextView) findViewById(R.id.tv_ring_interval);
        tvTime = (TextView) findViewById(R.id.tv_ring_time);
        tvLabel = (TextView) findViewById(R.id.tv_label_normal);
        tvVabrate = (TextView) findViewById(R.id.tv_vabrate_normal);
        tvWeek = (TextView) findViewById(R.id.tv_week_normal);
        rlInterval = (RelativeLayout) findViewById(R.id.ring_interval);
        rlTime = (RelativeLayout) findViewById(R.id.ring_time_normal);
        rlLabel = (RelativeLayout) findViewById(R.id.label_normal);
        rlVabrate = (RelativeLayout) findViewById(R.id.vabrate_normal);
        checkBox1 = (CheckBox) findViewById(R.id.c1);
        checkBox2 = (CheckBox) findViewById(R.id.c2);
        checkBox3 = (CheckBox) findViewById(R.id.c3);
        checkBox4 = (CheckBox) findViewById(R.id.c4);
        checkBox5 = (CheckBox) findViewById(R.id.c5);
        checkBox6 = (CheckBox) findViewById(R.id.c6);
        checkBox7 = (CheckBox) findViewById(R.id.c7);
        checkBox0 = (CheckBox) findViewById(R.id.c0);
        ivBack.setOnClickListener(this);
        rlInterval.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlLabel.setOnClickListener(this);
        rlVabrate.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        checkBox0.setOnCheckedChangeListener(this);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5.setOnCheckedChangeListener(this);
        checkBox6.setOnCheckedChangeListener(this);
        checkBox7.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.ring_interval:
                //设置点击后事件
                setRingIntervalClick();
                break;
            case R.id.ring_time_normal:
                setRingTimeClick();
                break;
            case R.id.label_normal:
                //设置标签
                setLabel();
                break;
            case R.id.vabrate_normal:
                //设默认开启1，点击一下即关0，即开
                if (vabrate == 1) {
                    vabrate = 0;
                    tvVabrate.setText("关");
                } else if (vabrate == 0) {
                    vabrate = 1;
                    tvVabrate.setText("开");
                }
                break;
            case R.id.btn_save_normal:
                Date now = new Date();
                //设置保存按钮，如果是新数据就保存，老数据就更新
                if (weekList.size() > 0) {
                    String s = weekList.toString();
                    week = s.substring(1, s.lastIndexOf("]"));
                    if (tag != 0) {
                        updateDatabase(tag);
                        finish();
                    } else {
                        Date date = new Date();
                        tag = (int) date.getTime();
                        saveToDatabase();
                        finish();
                    }
                } else {
                    //如果不重复，所选的事件应该大于当前时间
                    Date selectDate = new Date(now.getYear(), now.getMonth(), now.getDate(), hour, minute);
                    if (now.getTime() >= selectDate.getTime()) {
                        ToastUtls.showToast(this, "请选择大于当前的时间或选择重复周期");
                    } else {
                        week = "";
                        if (tag != 0) {
                            updateDatabase(tag);
                            finish();
                        } else {
                            Date date = new Date();
                            tag = (int) date.getTime();
                            saveToDatabase();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    private void setLabel() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("标签")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        label = editText.getText().toString();
                        tvLabel.setText(label);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    //默认每天时，其他不嫩点击
    private void defaultEveryday() {

        weekList.add("周一");
        weekList.add("周二");
        weekList.add("周三");
        weekList.add("周四");
        weekList.add("周五");
        weekList.add("周六");
        weekList.add("周日");
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBox5.setChecked(false);
        checkBox6.setChecked(false);
        checkBox7.setChecked(false);
        checkBox1.setClickable(false);
        checkBox2.setClickable(false);
        checkBox3.setClickable(false);
        checkBox4.setClickable(false);
        checkBox5.setClickable(false);
        checkBox6.setClickable(false);
        checkBox7.setClickable(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.c0:
                if (isChecked) {
                    defaultEveryday();
                    tvWeek.setText("每天");
                } else {
                    weekList.clear();
                    checkBox1.setClickable(true);
                    checkBox2.setClickable(true);
                    checkBox3.setClickable(true);
                    checkBox4.setClickable(true);
                    checkBox5.setClickable(true);
                    checkBox6.setClickable(true);
                    checkBox7.setClickable(true);
                }
                break;
            case R.id.c1:
                if (isChecked) {
                    weekList.add("周一");
                } else {
                    weekList.remove("周一");
                }
                break;
            case R.id.c2:
                if (isChecked) {
                    weekList.add("周二");
                } else {
                    weekList.remove("周二");
                }
                break;
            case R.id.c3:
                if (isChecked) {
                    weekList.add("周三");
                } else {
                    weekList.remove("周三");
                }
                break;
            case R.id.c4:
                if (isChecked) {
                    weekList.add("周四");
                } else {
                    weekList.remove("周四");
                }
                break;
            case R.id.c5:
                if (isChecked) {
                    weekList.add("周五");
                } else {
                    weekList.remove("周五");
                }
                break;
            case R.id.c6:
                if (isChecked) {
                    weekList.add("周六");
                } else {
                    weekList.remove("周六");
                }
                break;
            case R.id.c7:
                if (isChecked) {
                    weekList.add("周日");
                } else {
                    weekList.remove("周日");
                }
                break;
        }
        String s = "";
        for (int i = 0; i < weekList.size(); i++) {
            s += weekList.get(i) + " ";
        }
        if (weekList.size() == 7) {
            tvWeek.setText("每天");
        } else {
            if (s == "") {
                tvWeek.setText("不重复");
            } else {
                tvWeek.setText(s);
            }
        }
    }

    private void setRingIntervalClick() {

        View view = LayoutInflater.from(this).inflate(R.layout.interval_layout, null);
        final AlertDialog intervalDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(view)
                .create();
        intervalDialog.show();
        //设置圆角
        Window dialogWindow = intervalDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.interval_shape);
        //设置底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //设置距离底部参数
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.y = 20;
        dialogWindow.setAttributes(params);
        //初始化间隔对话框组件
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnSure = (Button) view.findViewById(R.id.btn_sure);
        //设置点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intervalDialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interval = sbInterval.getProgress();
                times = sbTimes.getProgress();
                tvInterval.setText(interval + "分钟," + times + "次");
                intervalDialog.dismiss();
            }
        });
        tvIntervalNumber = (TextView) view.findViewById(R.id.tv_interval_sb);
        sbInterval = (SeekBar) view.findViewById(R.id.seek_bar_interval);
        //设置seekbar点击事件
        sbInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvIntervalNumber.setText(String.valueOf(progress));
                int i = dip2px(NormalAlarmActivity.this, 30);
                int averagePx = (seekBar.getMeasuredWidth() - i) / 25;
                int y = dip2px(NormalAlarmActivity.this, 10);
                tvIntervalNumber.setX(seekBar.getX() + averagePx * (progress - 5) + y);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //初始化次数对话框组件
        tvTimesNumber = (TextView) view.findViewById(R.id.tv_times_sb);
        sbTimes = (SeekBar) view.findViewById(R.id.seek_bar_times);
        sbTimes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTimesNumber.setText(String.valueOf(progress));
                int i = dip2px(NormalAlarmActivity.this, 30);
                int averagePx = (seekBar.getMeasuredWidth() - i) / 9;
                int y = dip2px(NormalAlarmActivity.this, 10);
                tvTimesNumber.setX(seekBar.getX() + averagePx * (progress - 1) + y);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //Android坐标单位是px，dp需要转换为px
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setRingTimeClick() {
        timeDialog = new AlertDialog.Builder(this)
                .setTitle("闹钟时长")
                .setSingleChoiceItems(R.array.time_string, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                time = 1;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 1:
                                time = 5;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 2:
                                time = 10;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 3:
                                time = 15;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 4:
                                time = 20;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 5:
                                time = 25;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                            case 6:
                                time = 30;
                                tvTime.setText(String.valueOf(time));
                                timeDialog.dismiss();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        //设置圆角边框
        Window window = timeDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.interval_shape);
        //设置底部弹出
        window.setGravity(Gravity.BOTTOM);
        //设置距离底部距离
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 20;
        window.setAttributes(params);
        timeDialog.show();
    }


    private void updateDatabase(int tag) {
        ContentValues values = new ContentValues();
        values.put(Constants.WEEK, week);
        values.put(Constants.TYPE, type);
        values.put(Constants.LABEL, label);
        values.put(Constants.VABRATE, vabrate);
        values.put(Constants.HOUR, hour);
        values.put(Constants.MINUTE, minute);
        values.put(Constants.ENABLE, enable);
        values.put(Constants.TIME, time);
        values.put(Constants.TIMES, times);
        values.put(Constants.INTERVAL, interval);
        LoopDatabaseAccess.updateData("Loop", values, tag);
    }

    private void saveToDatabase() {
        ContentValues values = new ContentValues();
        values.put(Constants.WEEK, week);
        values.put(Constants.TYPE, type);
        values.put(Constants.LABEL, label);
        values.put(Constants.VABRATE, vabrate);
        values.put(Constants.HOUR, hour);
        values.put(Constants.MINUTE, minute);
        values.put(Constants.ENABLE, enable);
        values.put(Constants.TAG, tag);
        values.put(Constants.TIME, time);
        values.put(Constants.TIMES, times);
        values.put(Constants.INTERVAL, interval);
        LoopDatabaseAccess.insertData("Loop", values);
    }
}
