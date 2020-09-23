package com.example.loopalarmclock.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.example.loopalarmclock.AlarmViewActivity;

public class PendingService extends Service {
    public PendingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager pw = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pw.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, ":wo");
        wakeLock.acquire();
        String type = intent.getStringExtra("type");
        String period = null;

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

        Intent i = new Intent(this, LoopService.class);
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
        Log.d("PERIOD", type);
        if (type.equals("normal")) {
            period = intent.getStringExtra("period");
            i.putExtra("period",period);
        }
        startService(i);

        Log.d("PS", "1");
        //跳转intent
        Intent intent1 = new Intent(this, AlarmViewActivity.class);
        intent1.putExtra("time", time);
        intent1.putExtra("times", times);
        intent1.putExtra("interval", interval);
        intent1.putExtra("tag", tag);
        intent1.putExtra("label", label);
        intent1.putExtra("vibrate",vibrate);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent1);

        wakeLock.release();

        return super.onStartCommand(intent, flags, startId);
    }
}
