package com.example.loopalarmclock;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout llSkin, llSong, llHelp, llVersion;

    public MyLinearLayout(Context context) {
        super(context);
        this.context = context;
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llSkin = (LinearLayout) findViewById(R.id.skin);
        llSong = (LinearLayout) findViewById(R.id.song);
        llHelp = (LinearLayout) findViewById(R.id.help);
        llVersion = (LinearLayout) findViewById(R.id.version);
        llSong.setOnClickListener(this);
        llSkin.setOnClickListener(this);
        llVersion.setOnClickListener(this);
        llHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skin:
                Intent intent=new Intent(context,SkinActivity.class);
                context.startActivity(intent);
                break;
            case R.id.song:
                Intent intent1=new Intent(context, MediaPlayerActivity.class);
                context.startActivity(intent1);
                break;
            case R.id.help:
                break;
            case R.id.version:
                break;
        }
    }
}
