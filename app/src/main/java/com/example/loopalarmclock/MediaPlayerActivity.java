package com.example.loopalarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loopalarmclock.adapter.MusicAdapter;
import com.example.loopalarmclock.utils.ImmersiveStatusBar;
import com.example.loopalarmclock.utils.ReplaceSkinUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private ListView lv;
    private MusicAdapter adapter;
    //定义 请求返回码
    public static final int IMPORT_REQUEST_CODE = 10005;
    public String upLoadFilePath;
    public String upLoadFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        //沉浸式状态栏
        ImmersiveStatusBar.useImmersiveStatusBar(getWindow());
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
        ivBack = (ImageView) findViewById(R.id.iv_back_mediaplayer);

        lv = (ListView) findViewById(R.id.lv_mediaplayer);
        List<String> name = new ArrayList<>();
        name.add(getString(R.string.music1));
        name.add(getString(R.string.music2));
        name.add(getString(R.string.music3));
        name.add(getString(R.string.music4));
        name.add(getString(R.string.music5));
        name.add(getString(R.string.music6));
        name.add(getString(R.string.music7));
        name.add(getString(R.string.music8));
        name.add(getString(R.string.music9));
        name.add(getString(R.string.music10));
        name.add(getString(R.string.music11));
        name.add(getString(R.string.music12));

        //为ListView设置适配器
        adapter = new MusicAdapter(this, name);
        lv.setAdapter(adapter);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter.mediaPlayer != null) {
            adapter.mediaPlayer.stop();
            adapter.mediaPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_mediaplayer:
                finish();
                break;

        }
    }



}