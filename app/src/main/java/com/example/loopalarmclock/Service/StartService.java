package com.example.loopalarmclock.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.loopalarmclock.R;




public class StartService extends Service {



    public StartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String CHANNEL_ID = "com.example.test";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.app_name)).
                            setContentText("循环闹钟正在运行").
                            setWhen(System.currentTimeMillis()).
                            setSmallIcon(R.mipmap.ic_launcher).
                            setDefaults(NotificationCompat.FLAG_ONGOING_EVENT)
                    .setPriority(Notification.PRIORITY_MAX);
            Notification notification=builder.build();
            startForeground(1,notification);
        }
    }


}
