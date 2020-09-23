package com.example.loopalarmclock.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.loopalarmclock.R;

import java.util.HashMap;
import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private List<String> name;
    private Context context;
    //保存当前音乐数据的存储
    private SharedPreferences sharedPreferences;
    //记录被选中的RadioButton
    private static HashMap<Boolean, Integer> status = new HashMap<>();
    //用于存放音乐资源的数组
    int[] music = {R.raw.apologize, R.raw.becauseyoulovedme, R.raw.brokenangel,
            R.raw.callmemaybe, R.raw.christmasinmyheart, R.raw.couldthisbelove,
            R.raw.deutschland, R.raw.dilemma, R.raw.donotletmefall,
            R.raw.fallingstar, R.raw.icouldbetheone, R.raw.ifyoucometome};
    public MediaPlayer mediaPlayer;
    private final SharedPreferences.Editor editor;

    public MusicAdapter(Context context, List<String> name) {
        this.context = context;
        this.name = name;
        sharedPreferences=context.getSharedPreferences("music",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //获取存储的音乐资源索引如果不存在默认选中第一首
        status.put(true, sharedPreferences.getInt("index",0));
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_mediaplayer, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvMusicName.setText(name.get(position));
        viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status.clear();
                    status.put(true, position);
                }
                notifyDataSetChanged();
            }
        });
        //根据status判断那个需要选中和bu选中
        if (status.get(true) == position) {
            viewHolder.radioButton.setChecked(true);
            editor.clear();
            editor.putInt("index",position);
            editor.commit();
            playMusic(position);
        } else {
            viewHolder.radioButton.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder {
        private RadioButton radioButton;
        private TextView tvMusicName;

        public ViewHolder(View view) {
            radioButton = view.findViewById(R.id.rb_item_media);
            tvMusicName = view.findViewById(R.id.tv_music_name);
        }
    }
    //选中时播放音乐
    public void playMusic(int position){
        if (mediaPlayer!=null){
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(context,music[position]);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
}

