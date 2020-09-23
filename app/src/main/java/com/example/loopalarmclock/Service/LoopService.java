package com.example.loopalarmclock.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoopService extends Service {


    public LoopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String type = intent.getStringExtra("type");
        String[] week = intent.getStringArrayExtra("week");
        int dh = intent.getIntExtra("dh", 0);
        int dm = intent.getIntExtra("dm", 0);
        int dd = intent.getIntExtra("dd", 0);
        int time = intent.getIntExtra("time", 0);
        int times = intent.getIntExtra("times", 0);
        int interval = intent.getIntExtra("interval", 0);
        String label = intent.getStringExtra("label");
        int tag = intent.getIntExtra("tag", 0);
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);
        int vibrate=intent.getIntExtra("vibrate",0);
        String period = null;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, PendingService.class);
        i.putExtra("type", type);
        i.putExtra("label", label);
        i.putExtra("time", time);
        i.putExtra("times", times);
        i.putExtra("interval", interval);
        i.putExtra("tag", tag);
        i.putExtra("dd", dd);
        i.putExtra("dh", dh);
        i.putExtra("dm", dm);
        i.putExtra("week", week);
        i.putExtra("hour", hour);
        i.putExtra("minute", minute);
        i.putExtra("vibrate",vibrate);

        PendingIntent pi = PendingIntent.getBroadcast(this, tag, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if (intent.getStringExtra("type").equals("loop")) {
            if (dd != 0 || dm != 0 || dh != 0) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + dd * 24 * 60 * 60 * 1000 + dh * 60 * 60 * 1000 + dm * 60 * 1000, pi);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + dd * 24 * 60 * 60 * 1000 + dh * 60 * 60 * 1000 + dm * 60 * 1000, pi);
                }
            }
        } else if (intent.getStringExtra("type").equals("normal")) {
            period = intent.getStringExtra("period");
            i.putExtra("period", period);
            if (week.length > 0 && week.length <= 7) {
                int requestCode = 0;
                switch (period) {
                    case "周一":
                        requestCode = Integer.parseInt("1" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case "周二":
                        requestCode = Integer.parseInt("2" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case "周三":
                        requestCode = Integer.parseInt("3" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case "周四":
                        requestCode = Integer.parseInt("4" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case "周五":
                        requestCode = Integer.parseInt("5" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case "周六":
                        requestCode = Integer.parseInt("6" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case " 周日":
                        requestCode = Integer.parseInt("7" + String.valueOf(hour) + String.valueOf(minute));
                        pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                }

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd,HH-mm-ss");
                    Date d=new Date();
                    d.setTime(System.currentTimeMillis()+7 * 24 * 60 * 60 * 1000);

                    Log.d("12",simpleDateFormat.format(d)+"/"+d.getTime()+"/"+System.currentTimeMillis());
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000, pi);
                }

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}