package com.example.loopalarmclock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.loopalarmclock.R;

public class ReplaceSkinUtils {


    public static void replaceSkin(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("skin", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("bg", 4) == 0) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.bg);
        } else if (sharedPreferences.getInt("bg", 4) == 1) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.bg2);
        } else if (sharedPreferences.getInt("bg", 4) == 2) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.bg3);
        } else if (sharedPreferences.getInt("bg", 4) == 3) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.bg4);
        } else if (sharedPreferences.getInt("bg", 4) == 4) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.bg5);
        } else if (sharedPreferences.getInt("bg", 4) == 5) {
            activity.getWindow().setBackgroundDrawableResource(R.drawable.holiday_bg);
        }
    }
}
