package com.example.loopalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.loopalarmclock.Service.PendingService;
import com.example.loopalarmclock.adapter.MusicAdapter;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.utils.ImmersiveStatusBar;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmViewActivity extends Activity implements View.OnClickListener {

    private TextView tvTime, tvDate, tvLabel;
    private Button btnClose, btnAfter10;
    private String label;

    private int time;
    private int times;
    private int interval;
    private int tag;
    private AlarmManager am;
    private PendingIntent pi;
    private Intent intent;
    private MediaPlayer mp;
    private Timer timer;
    //用于存放音乐资源的数组
    int[] music = {R.raw.apologize, R.raw.becauseyoulovedme, R.raw.brokenangel,
            R.raw.callmemaybe, R.raw.christmasinmyheart, R.raw.couldthisbelove,
            R.raw.deutschland, R.raw.dilemma, R.raw.donotletmefall,
            R.raw.fallingstar, R.raw.icouldbetheone, R.raw.ifyoucometome};

    private static PowerManager.WakeLock wakeLock;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

        wakeLock = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getCanonicalName());
        wakeLock.acquire();


        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext()
                .getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock unlock = keyguardManager.newKeyguardLock("unlock");
        unlock.disableKeyguard();
        //显示在锁屏界面上
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        setContentView(R.layout.activity_alarm_view);

//沉浸式状态栏
        Window window = getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        timer = new Timer();
        tvTime = (TextView) findViewById(R.id.tv_hour_alarmview);
        tvDate = (TextView) findViewById(R.id.tv_date_alarmview);
        tvLabel = (TextView) findViewById(R.id.tv_label_alarmview);
        btnClose = (Button) findViewById(R.id.btn_close_alarmview);
        btnAfter10 = (Button) findViewById(R.id.btn_after10);
        btnClose.setOnClickListener(this);
        btnAfter10.setOnClickListener(this);
        //检查是否从获取音乐资源索引
        int index=getSharedPreferences("music",MODE_PRIVATE).getInt("index",0);
        mp= MediaPlayer.create(this,music[index]);
        mp.setLooping(true);
        mp.start();


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        tvTime.setText(hour + ":" + minute);
        tvDate.setText(year + "年" + month + "月" + day + "日");

        Intent i = getIntent();
        final int vibrate = i.getIntExtra("vibrate", 0);
        time = i.getIntExtra("time", 0);
        times = i.getIntExtra("times", 0);
        interval = i.getIntExtra("interval", 0);
        label = i.getStringExtra("label");
        tag = i.getIntExtra("tag", 0);
        final Intent intent = new Intent("com.example.alarmviewactivity");
        if (vibrate == 1) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{1000, 1000}, 1);
        }

        am = (AlarmManager) getSystemService(ALARM_SERVICE);


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (times - 1 > 0) {
                    times--;
                    intent.putExtra("vibrate", vibrate);
                    intent.putExtra("times", times);
                    intent.putExtra("time", time);
                    intent.putExtra("label", label);
                    intent.putExtra("interval", interval);
                    intent.putExtra("tag", tag);
                    pi = PendingIntent.getActivity(AlarmViewActivity.this, tag, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, interval * 60 * 1000 + System.currentTimeMillis(), pi);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, interval * 60 * 1000 + System.currentTimeMillis(), pi);
                    }
                }
                finish();

            }
        }, 60 * 1000);
        tvLabel.setText(label);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vibrator != null) {
            vibrator.cancel();
        }
        wakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close_alarmview:
                finish();
                break;
            case R.id.btn_after10:
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        intent = new Intent("com.example.alarmviewactivity");
                        intent.putExtra("label", label);
                        intent.putExtra("time", time);
                        intent.putExtra("times", times);
                        intent.putExtra("interval", interval);
                        intent.putExtra("tag", tag);
                        startActivity(intent);
                    }
                };
                timer.schedule(timerTask, 10 * 60 * 1000);
                finish();
                break;
        }
    }


}
