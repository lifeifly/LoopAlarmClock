package com.example.loopalarmclock.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import com.example.loopalarmclock.Service.PendingService;

import com.example.loopalarmclock.Service.StartService;
import com.example.loopalarmclock.bean.LoopAlarmBean;

import java.util.Calendar;

public class StartCloseAlarm {

    private final AlarmManager am;
    private Context context;

    private StartCloseAlarm(Context context) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    //单例模式
    private static StartCloseAlarm startCloseAlarm;

    public static StartCloseAlarm getInstance(Context context) {
        if (startCloseAlarm == null) {
            startCloseAlarm = new StartCloseAlarm(context);
        }
        return startCloseAlarm;
    }

    //启动计时任务
    public void startAlarm(LoopAlarmBean bean) {

        String[] s = bean.getWeek().split(",");
        Intent i = new Intent(context, PendingService.class);
        i.putExtra("type", bean.getType());
        i.putExtra("label", bean.getLabel());
        i.putExtra("time", bean.getTime());
        i.putExtra("times", bean.getTimes());
        i.putExtra("interval", bean.getInterval());
        i.putExtra("tag", bean.getTag());
        i.putExtra("dd", bean.getDd());
        i.putExtra("dh", bean.getDh());
        i.putExtra("dm", bean.getDm());
        i.putExtra("week", s);
        i.putExtra("hour", bean.getHour());
        i.putExtra("minute", bean.getMinute());
        i.putExtra("vibrate", bean.getVabrate());
        PendingIntent pendingIntent = PendingIntent.getService(context, bean.getTag(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar currentCalendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if (bean.getType().equals("loop")) {

            if (bean.getYear() != 0) {
                c.set(bean.getYear(), bean.getMonth(), bean.getDay(), bean.getHour(), bean.getMinute());
                c.set(Calendar.SECOND, 0);
            } else {
                c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                c.set(Calendar.MINUTE, bean.getMinute());
                c.set(Calendar.SECOND, 0);
            }
            if (currentCalendar.getTimeInMillis() < c.getTimeInMillis()) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                }
            } else {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
                }
            }
        } else if (bean.getType().equals("normal")) {
            PendingIntent pi = null;
            if (s.length > 0 && s.length <= 7) {
                for (int j = 0; j < s.length; j++) {
                    int requestCode = 0;
                    switch (s[j]) {
                        case "周一":
                            i.putExtra("period", "周一");
                            c.set(Calendar.DAY_OF_WEEK, 2);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            Log.d("TAG", c.get(Calendar.MINUTE) + "/" + c.get(Calendar.DAY_OF_WEEK) + "/" + c.get(Calendar.HOUR_OF_DAY));
                            requestCode = Integer.parseInt("1" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            break;
                        case " 周二":
                            i.putExtra("period", "周二");
                            requestCode = Integer.parseInt("2" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 3);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            Log.d("TAG", c.get(Calendar.MINUTE) + "/" + c.get(Calendar.DAY_OF_WEEK) + "/" + c.get(Calendar.HOUR_OF_DAY));
                            break;
                        case " 周三":
                            i.putExtra("period", "周三");
                            requestCode = Integer.parseInt("3" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 4);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            break;
                        case " 周四":
                            i.putExtra("period", "周四");
                            requestCode = Integer.parseInt("4" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 5);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            break;
                        case " 周五":
                            i.putExtra("period", "周五");
                            requestCode = Integer.parseInt("5" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 6);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            break;
                        case " 周六":
                            i.putExtra("period", "周六");
                            requestCode = Integer.parseInt("6" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 7);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            break;
                        case " 周日":
                            i.putExtra("period", "周日");
                            requestCode = Integer.parseInt("7" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                            pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            c.set(Calendar.DAY_OF_WEEK, 1);
                            c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                            c.set(Calendar.MINUTE, bean.getMinute());
                            c.set(Calendar.SECOND, 0);
                            break;
                    }
                    if (currentCalendar.getTimeInMillis() < c.getTimeInMillis()) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            Log.d("TAG", c.getTimeInMillis() + "?" + System.currentTimeMillis());
                            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                        } else {
                            am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                        }
                    } else {

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000, pi);
                        } else {
                            am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000, pi);
                        }
                    }
                }
            } else if (s.length == 0) {
                c.set(Calendar.HOUR_OF_DAY, bean.getHour());
                c.set(Calendar.MINUTE, bean.getMinute());
                if (currentCalendar.getTimeInMillis() < c.getTimeInMillis()) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    } else {
                        am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    }
                } else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
                    } else {
                        am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pendingIntent);
                    }
                }
            }
        }
        Intent intent=new Intent(context, StartService.class);
        context.startService(intent);
    }

    //取消计时任务
    public void cancelAlarm(LoopAlarmBean bean) {
        Intent i = new Intent(context, PendingService.class);
        PendingIntent pi = null;
        int requestCode = 0;
        if (bean.getType().equals("normal")) {
            String[] split = bean.getWeek().split(",");
            for (int j = 0; j < split.length; j++) {
                switch (split[j]) {
                    case "周一":
                        requestCode = Integer.parseInt("1" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case " 周二":
                        requestCode = Integer.parseInt("2" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case " 周三":

                        requestCode = Integer.parseInt("3" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    case " 周四":
                        requestCode = Integer.parseInt("4" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case " 周五":

                        requestCode = Integer.parseInt("5" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case " 周六":

                        requestCode = Integer.parseInt("6" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                    case " 周日":

                        requestCode = Integer.parseInt("7" + String.valueOf(bean.getHour()) + String.valueOf(bean.getMinute()));
                        pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        break;
                }
                am.cancel(pi);
            }

        } else {
            pi = PendingIntent.getService(context, bean.getTag(), i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pi);
        }
//取消提醒界面设置的间隔
        Intent intent = new Intent("com.example.alarmviewactivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, bean.getTag(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }
}
