package com.example.loopalarmclock.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.example.loopalarmclock.R;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.database.LoopDatabaseAccess;


import java.util.ArrayList;
import java.util.List;

public class DeleteListViewAdapter extends BaseAdapter {
    private Context context;
    private List<LoopAlarmBean> data;
    //选中个数
    private int number=0;



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public DeleteListViewAdapter(Context context, List<LoopAlarmBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_delete, parent, false);
            viewHolder = new ViewHolder(convertView,position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //绑定数据
        LoopAlarmBean bean = data.get(position);
        viewHolder.tvLabel.setText(bean.getLabel());
        viewHolder.tvTime.setText(bean.getHour() + ":" + bean.getMinute());
        if (bean.getType().equals("loop")){
            viewHolder.tvType.setText("间隔闹钟");
            if (bean.getDd() == 0 && bean.getDh() == 0 && bean.getDm() == 0) {
                viewHolder.tvCategory.setText("一次闹钟");
            } else {
                viewHolder.tvCategory.setText("间隔: " + bean.getDd() + "天" + bean.getDh() + "小时" + bean.getDm() + "分钟");
            }
        }else if (bean.getType().equals("normal")){
            viewHolder.tvType.setText("普通闹钟");
            if (bean.getWeek()==null) {
                viewHolder.tvCategory.setText("不重复");
            } else {
                StringBuilder str= new StringBuilder();
                String[] s=bean.getWeek().split(",");
                if (s.length==7){
                    viewHolder.tvCategory.setText("重复:"+"每天");
                }else if (s.length>0&&s.length<7){
                    for (String value : s) {
                        str.append(value).append(" ");
                    }
                    viewHolder.tvCategory.setText("重复:"+str);
                }else {
                    viewHolder.tvCategory.setText("不重复");
                }
            }
        }
        viewHolder.cbLv.setChecked(bean.isSelected());
        return convertView;
    }

    class ViewHolder {
        private TextView tvTime, tvLabel, tvCategory,tvType;
        private CheckBox cbLv;

        public ViewHolder(View itemView, final int positon) {
            tvLabel = (TextView) itemView.findViewById(R.id.tv_label_lv);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time_lv);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category_lv);
            cbLv = (CheckBox) itemView.findViewById(R.id.cb_lv);
            tvType=(TextView)itemView.findViewById(R.id.tv_type);
            cbLv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.get(positon).setSelected(isChecked);
                    if (isChecked){
                        //被选中
                        number++;
                    }else {
                        //取消选中
                        number--;
                    }
                }
            });
        }

    }

    //删除数据
    public void deleteData() {
        for (int i=0;i<data.size();i++) {
            LoopAlarmBean bean=data.get(i);
            if (bean.isSelected()) {
                LoopDatabaseAccess.deleteItemData("Loop", String.valueOf(bean.getTag()));
                data.remove(i);
                notifyDataSetChanged();
                i--;
            }
        }
        number=0;
    }
    //全选或全不选
    public void selectAllOrNone(boolean isChecked){
        for (LoopAlarmBean bean:data){
            if (bean.isSelected()!=isChecked){
                bean.setSelected(isChecked);
                notifyDataSetChanged();
            }
        }
    }
    //检查是否全选
    public boolean checkCheckedAllItem(){
        if (data.size() > 0){
            //选中的条目数
            int number=0;
            for (LoopAlarmBean bean:data){
                if (bean.isSelected()){
                    number++;
                }
            }
            if (number==data.size()){
                //全选
                return true;
            }else {
                //非全选
                return false;
            }
        }
        return false;
    }
}
