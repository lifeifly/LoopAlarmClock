package com.example.loopalarmclock;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.loopalarmclock.utils.ImmersiveStatusBar;
import com.example.loopalarmclock.utils.ReplaceSkinUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinActivity extends Activity {
    private GridView gvSkin;
    private ImageView ivBack;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        //更换背景
        ReplaceSkinUtils.replaceSkin(this);
        sharedPreferences=getSharedPreferences("skin",MODE_PRIVATE);
        //沉浸式状态栏
        Window window=getWindow();
        ImmersiveStatusBar.useImmersiveStatusBar(window);
        final WallpaperManager wallpaperManager= (WallpaperManager) getSystemService(WALLPAPER_SERVICE);
        ivBack = (ImageView) findViewById(R.id.iv_back_skin);
        gvSkin = (GridView) findViewById(R.id.gv_skin);
        int[] image = {R.drawable.bg, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5,R.drawable.holiday_bg};
        String[] imageName = {"温暖绿色", "粉墙玉砌", "恍如灯火","晴空万里", "灰色孤独","夕阳无限"};
        final List<Map<String, Object>> data = new ArrayList<>();
        //添加数据
        for (int i = 0; i < imageName.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image[i]);
            map.put("imageName", imageName[i]);
            data.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item_gv_skin,
                new String[]{"image", "imageName"}, new int[]{R.id.iv_item_gv, R.id.tv_item_skin});
        gvSkin.setAdapter(simpleAdapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置GridView点击事件
        gvSkin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("bg",position);
                editor.commit();
                Intent intent=new Intent(SkinActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
