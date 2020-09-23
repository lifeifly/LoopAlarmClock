package com.example.loopalarmclock.utils;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class ImmersiveStatusBar {
    public static void useImmersiveStatusBar(Window window){
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT>=4.4){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
