package com.example.loopalarmclock.BroadcastReiceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.loopalarmclock.database.LoopDatabaseAccess;

public class AutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LoopDatabaseAccess.queryAllData("Loop");
    }
}
