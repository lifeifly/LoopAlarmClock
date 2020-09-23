package com.example.loopalarmclock.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.loopalarmclock.R;

import com.example.loopalarmclock.bean.Constants;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.database.LoopDatabaseAccess;



import java.util.List;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<LoopAlarmBean> data;



    public AlarmRecyclerViewAdapter(Context context, List<LoopAlarmBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final ViewHolder viewHolder = (ViewHolder) holder;
        final LoopAlarmBean bean = (LoopAlarmBean) data.get(position);
        switch (bean.getType()) {
            case "loop":
                viewHolder.tvType.setText("间隔闹钟");
                if (bean.getDd() == 0 && bean.getDh() == 0 && bean.getDm() == 0) {
                    viewHolder.tvCategoty.setText("一次闹钟");
                } else {
                    viewHolder.tvCategoty.setText("间隔: " + bean.getDd() + "天" + bean.getDh() + "小时" + bean.getDm() + "分钟");
                }
                break;
            case "normal":
                viewHolder.tvType.setText("普通闹钟");
                if (bean.getWeek()==null) {
                    viewHolder.tvCategoty.setText("不重复");
                } else {
                    String str="";
                    String[] s=bean.getWeek().split(",");
                    if (s.length==7){
                        viewHolder.tvCategoty.setText("重复:"+"每天");
                    }else if (s.length>0&&s.length<7){
                        for (int i = 0; i <s.length; i++) {
                            str+=s[i]+" ";
                        }
                        viewHolder.tvCategoty.setText("重复:"+str);
                    }else {
                        viewHolder.tvCategoty.setText("不重复");
                    }
                }
                break;

        }
        viewHolder.tvLabel.setText(bean.getLabel());
        viewHolder.tvTime.setText(bean.getHour() + ":" + bean.getMinute());

        if (bean.getEnable() == 1) {
            viewHolder.ivOn.setVisibility(View.VISIBLE);
            viewHolder.ivOff.setVisibility(View.GONE);
        } else if (bean.getEnable() == 0) {
            viewHolder.ivOn.setVisibility(View.GONE);
            viewHolder.ivOff.setVisibility(View.VISIBLE);
        }
        viewHolder.switchFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getEnable() == 0) {
                    viewHolder.ivOff.setVisibility(View.GONE);
                    viewHolder.ivOn.setVisibility(View.VISIBLE);
                    bean.setEnable(1);
                    //开启闹钟并刷新数据库
                    Log.d("START", String.valueOf(bean.getTag()) + "1");
                    ContentValues values = new ContentValues();
                    values.put(Constants.ENABLE, 1);
                    LoopDatabaseAccess.updateData(Constants.LOOP, values, bean.getTag());
                } else if (bean.getEnable() == 1) {
                    Log.d("CLOSE", String.valueOf(bean.getTag()) + "1");
                    viewHolder.ivOff.setVisibility(View.VISIBLE);
                    viewHolder.ivOn.setVisibility(View.GONE);
                    bean.setEnable(0);
                    //关闭闹钟并刷新数据库
                    ContentValues values = new ContentValues();
                    values.put(Constants.ENABLE, 0);
                    LoopDatabaseAccess.updateData(Constants.LOOP, values, bean.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel, tvTime, tvCategoty, tvType;
        private FrameLayout switchFl;
        private ImageView ivOn, ivOff;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvCategoty = (TextView) itemView.findViewById(R.id.tv_category);
            switchFl = (FrameLayout) itemView.findViewById(R.id.switchFl);
            ivOff = (ImageView) itemView.findViewById(R.id.switch_off);
            ivOn = (ImageView) itemView.findViewById(R.id.switch_on);

            //点击进入编辑页面
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(v, getLayoutPosition());
                    }
                }
            });
            //长点击进入删除页面
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemLongClick(v, getLayoutPosition());
                    }
                    return true;
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnItemLongClick(View v, int position);
    }


}
