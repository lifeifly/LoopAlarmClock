package com.example.loopalarmclock.loop;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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


import java.util.Calendar;

import java.util.Date;


public class LoopAlarmAcitivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private String type = "loop";
    private TimePicker timePicker;
    private RelativeLayout rl_date, rl_label, rl_vabrate, rl_ringtime, rl_ringinterval;
    private TextView tvYear, tvMonth, tvDay, tvRingTime, tvRingInterval;
    private TextView tv_label_loop, vabrate_vabrate_loop;
    private ImageView iv_back_titlebar;
    private TextView tvSave;
    private SeekBar hourSeekBar, minuteSeekBar, daySeekBar;
    //间隔事件显示组件
    private TextView tvDuHour, tvDuMinute, tvDuDay;
    //数据库需要的数据
    private int hour, minute, year, month, day, duDay, duHour, duMinute;
    private int ringTime = 5, ringInterval = 10, ringTimes = 3;
    //默认震动开为1
    private int vabrate = 1;
    private String label = "标签";
    //默认开启
    private int enable = 1;
    private int tag;


    //对话框组件
    private TextView tvIntervalNumber, tvTimesNumber;
    private SeekBar sbInterval, sbTimes;
    private Button btnCancel, btnSure;
    private AlertDialog timeDialog;
    private LoopAlarmBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_alarm);
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
//沉浸式状态栏
        Window window=getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        initView();
        //获取数据
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            bean = (LoopAlarmBean) bundle.getSerializable("bean");
            year = bean.getYear();
            month = bean.getMonth();
            day = bean.getDay();
            hour = bean.getHour();
            minute = bean.getMinute();
            tag = bean.getTag();
            vabrate = bean.getVabrate();
            duDay = bean.getDd();
            duHour = bean.getDh();
            duMinute = bean.getDm();
            ringTime = bean.getTime();
            ringTimes = bean.getTimes();
            ringInterval = bean.getInterval();
            enable = bean.getEnable();

            tvYear.setText(String.valueOf(bean.getYear()));
            tvMonth.setText(String.valueOf(bean.getMonth()));
            tvDay.setText(String.valueOf(bean.getDay()));
            tvDuDay.setText(String.valueOf(bean.getDd()));
            tvDuHour.setText(String.valueOf(bean.getDh()));
            tvDuMinute.setText(String.valueOf(bean.getDm()));
            tv_label_loop.setText(bean.getLabel());
            hourSeekBar.setProgress(bean.getDh());
            daySeekBar.setProgress(bean.getDd());
            minuteSeekBar.setProgress(bean.getDm());
            timePicker.setCurrentHour(bean.getHour());
            timePicker.setCurrentMinute(bean.getMinute());

            tvRingInterval.setText(ringInterval + "分钟," + ringTimes + "次");
            tvRingTime.setText(String.valueOf(ringTime));

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
                ringInterval = sbInterval.getProgress();
                ringTimes = sbTimes.getProgress();
                tvRingInterval.setText(ringInterval + "分钟," + ringTimes + "次");
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
                int i = dip2px(LoopAlarmAcitivity.this, 30);
                int averagePx = (seekBar.getMeasuredWidth() - i) / 25;
                int y = dip2px(LoopAlarmAcitivity.this, 10);
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
                int i = dip2px(LoopAlarmAcitivity.this, 30);
                int averagePx = (seekBar.getMeasuredWidth() - i) / 9;
                int y = dip2px(LoopAlarmAcitivity.this, 10);
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

    private void initView() {


        iv_back_titlebar = (ImageView) findViewById(R.id.iv_back_titlebar);
        tvSave = (TextView) findViewById(R.id.btn_save);

        rl_date = (RelativeLayout) findViewById(R.id.date_loop);
        rl_label = (RelativeLayout) findViewById(R.id.label_loop);
        rl_vabrate = (RelativeLayout) findViewById(R.id.vabrate_loop);
        rl_ringtime = (RelativeLayout) findViewById(R.id.ring_time);
        rl_ringinterval = (RelativeLayout) findViewById(R.id.ring_interval);
        tvRingTime = (TextView) findViewById(R.id.tv_ring_time);
        tvRingInterval = (TextView) findViewById(R.id.tv_ring_interval);

        timePicker = (TimePicker) findViewById(R.id.timer_picker);

        tvYear = (TextView) findViewById(R.id.year_date_loop);
        tvMonth = (TextView) findViewById(R.id.month_date_loop);
        tvDay = (TextView) findViewById(R.id.day_date_loop);

        tv_label_loop = (TextView) findViewById(R.id.tv_label_loop);
        vabrate_vabrate_loop = (TextView) findViewById(R.id.vabrate_vabrate_loop);

        tvDuDay = (TextView) findViewById(R.id.tv_day);
        tvDuHour = (TextView) findViewById(R.id.tv_hour);
        tvDuMinute = (TextView) findViewById(R.id.tv_minute);

        hourSeekBar = (SeekBar) findViewById(R.id.hour_seekbar);
        minuteSeekBar = (SeekBar) findViewById(R.id.mimute_seekbar);
        daySeekBar = (SeekBar) findViewById(R.id.day_seekbar);
        //设置监听拖动
        hourSeekBar.setOnSeekBarChangeListener(this);
        minuteSeekBar.setOnSeekBarChangeListener(this);
        daySeekBar.setOnSeekBarChangeListener(this);

        tvSave.setOnClickListener(this);
        iv_back_titlebar.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        rl_label.setOnClickListener(this);
        rl_vabrate.setOnClickListener(this);
        rl_ringinterval.setOnClickListener(this);
        rl_ringtime.setOnClickListener(this);
        setTimerPicker();
        setVabrate();


    }


    private void setVabrate() {
        if (vabrate == 0) {
            vabrate_vabrate_loop.setText("关");
        } else if (vabrate == 1) {
            vabrate_vabrate_loop.setText("开");
        }
    }

    private void setTimerPicker() {
        //设置24小时制
        timePicker.setIs24HourView(true);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        //设置监听器
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minu) {
                hour = hourOfDay;
                minute = minu;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                //设置保存按钮，如果是新数据就保存，老数据就更新
                setSave();
                Log.d("RE", type+","+year+","+tag);
                break;
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.date_loop:
                //日期选择并展示
                selectDate();
                break;
            case R.id.label_loop:
                //设置标签
                setLabel();
                break;
            case R.id.vabrate_loop:
                //设默认开启1，点击一下即关0，即开
                if (vabrate == 1) {
                    vabrate = 0;
                    vabrate_vabrate_loop.setText("关");
                } else if (vabrate == 0) {
                    vabrate = 1;
                    vabrate_vabrate_loop.setText("开");
                }
                break;
            case R.id.ring_interval:
                //设置点击后事件
                setRingIntervalClick();
                break;
            case R.id.ring_time:
                //设置点击后事件
                setRingTImeClick();
                break;
        }
    }


    private void setSave() {
        if (tag != 0) {
            //更新数据库
            updateDatabase(tag);
            finish();
        } else {
            Date currentDate = new Date();
            if (year == 0 && month == 0 && day == 0) {
                tag = (int) currentDate.getTime();
                //保存数据到数据库
                saveToDatabase();
                //退出该activity
                finish();
            } else {
                if (year < (currentDate.getYear() + 1900)) {
                    Toast.makeText(this, "请选择起始日期大于当前日期的时间", Toast.LENGTH_LONG).show();
                } else if (year == (currentDate.getYear() + 1900)) {
                    if (month == (currentDate.getMonth() + 1)) {
                        if (day == currentDate.getDate()) {
                            Date date = new Date(year, month, day, hour, minute);
                            if (currentDate.getTime() >= date.getTime()) {
                                Toast.makeText(this, "请选择起始日期大于当前日期的时间", Toast.LENGTH_LONG).show();
                            } else {
                                tag = (int) currentDate.getTime();
                                //保存数据到数据库
                                saveToDatabase();
                                //退出该activity
                                finish();
                            }
                        } else if (day < currentDate.getDate()) {
                            Toast.makeText(this, "请选择起始日期大于当前日期的时间", Toast.LENGTH_LONG).show();
                        } else if (day > currentDate.getDate()) {
                            tag = (int) currentDate.getTime();
                            //保存数据到数据库
                            saveToDatabase();
                            //退出该activity
                            finish();
                        }
                    } else if (month < (currentDate.getMonth() + 1)) {
                        Toast.makeText(this, "请选择起始日期大于当前日期的时间", Toast.LENGTH_LONG).show();
                    } else if (month > (currentDate.getMonth() + 1)) {

                        tag = (int) currentDate.getTime();
                        //保存数据到数据库
                        saveToDatabase();
                        //退出该activity
                        finish();
                    }
                } else if (year > (currentDate.getYear() + 1900)) {
                    tag = (int) currentDate.getTime();
                    //保存数据到数据库
                    saveToDatabase();
                    //退出该activity
                    finish();
                }
            }
        }
    }

    private void updateDatabase(int tag) {
        ContentValues values = new ContentValues();
        values.put(Constants.TYPE, type);
        values.put(Constants.LABEL, label);
        values.put(Constants.VABRATE, vabrate);
        values.put(Constants.YEAR, year);
        values.put(Constants.MONTH, month);
        values.put(Constants.DAY, day);
        values.put(Constants.HOUR, hour);
        values.put(Constants.MINUTE, minute);
        values.put(Constants.DURATION_DAY, duDay);
        values.put(Constants.DURATION_HOUR, duHour);
        values.put(Constants.DURATION_MINUTE, duMinute);
        values.put(Constants.ENABLE, enable);
        values.put(Constants.TIME, ringTime);
        values.put(Constants.TIMES, ringTimes);
        values.put(Constants.INTERVAL, ringInterval);
        LoopDatabaseAccess.updateData("Loop", values, tag);
    }

    private void saveToDatabase() {
        ContentValues values = new ContentValues();
        values.put(Constants.TYPE, type);
        values.put(Constants.LABEL, label);
        values.put(Constants.VABRATE, vabrate);
        values.put(Constants.YEAR, year);
        values.put(Constants.MONTH, month);
        values.put(Constants.DAY, day);
        values.put(Constants.HOUR, hour);
        values.put(Constants.MINUTE, minute);
        values.put(Constants.DURATION_DAY, duDay);
        values.put(Constants.DURATION_HOUR, duHour);
        values.put(Constants.DURATION_MINUTE, duMinute);
        values.put(Constants.ENABLE, enable);
        values.put(Constants.TAG, tag);
        values.put(Constants.TIME, ringTime);
        values.put(Constants.TIMES, ringTimes);
        values.put(Constants.INTERVAL, ringInterval);
        LoopDatabaseAccess.insertData("Loop", values);
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
                        tv_label_loop.setText(label);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }


    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yea, int mont, int dayOfMonth) {
                year = yea;
                month = mont + 1;
                day = dayOfMonth;
                tvYear.setText(String.valueOf(year));
                tvMonth.setText(String.valueOf(month));
                tvDay.setText(String.valueOf(dayOfMonth));
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.hour_seekbar:
                if (progress < 24) {
                    duHour = progress;
                    tvDuHour.setText(String.valueOf(duHour));
                } else {
                    if (daySeekBar.getProgress() < 30) {
                        duDay = daySeekBar.getProgress() + 1;
                        duHour = 0;
                        daySeekBar.setProgress(daySeekBar.getProgress() + 1);
                        tvDuHour.setText(String.valueOf(duHour));
                        tvDuDay.setText(String.valueOf(duDay));
                        //设置hourseekbar为0
                        hourSeekBar.setProgress(0);
                    }
                }
                break;
            case R.id.mimute_seekbar:
                if (progress < 60) {
                    duMinute = progress;
                    tvDuMinute.setText(String.valueOf(duMinute));
                } else {
                    if (hourSeekBar.getProgress() < 60) {
                        duHour = hourSeekBar.getProgress() + 1;
                        duMinute = 0;
                        hourSeekBar.setProgress(daySeekBar.getProgress() + 1);
                        tvDuMinute.setText(String.valueOf(duMinute));
                        tvDuHour.setText(String.valueOf(duHour));
                        //设置hourseekbar为0
                        minuteSeekBar.setProgress(0);
                    }
                }
                break;
            case R.id.day_seekbar:
                duDay = progress;
                tvDuDay.setText(String.valueOf(duDay));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private void setRingTImeClick() {
        timeDialog = new AlertDialog.Builder(this)
                .setTitle("闹钟时长")
                .setSingleChoiceItems(R.array.time_string, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ringTime = 1;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 1:
                                ringTime = 5;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 2:
                                ringTime = 10;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 3:
                                ringTime = 15;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 4:
                                ringTime = 20;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 5:
                                ringTime = 25;
                                tvRingTime.setText(String.valueOf(ringTime));
                                timeDialog.dismiss();
                                break;
                            case 6:
                                ringTime = 30;
                                tvRingTime.setText(String.valueOf(ringTime));
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

    }
}
