package com.example.loopalarmclock.app;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Window;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.loopalarmclock.database.LoopDatabaseAccess;
import com.example.loopalarmclock.utils.ImmersiveStatusBar;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //数据库跟随项目一并初始化
        LoopDatabaseAccess.initDB(this);

    }



}
