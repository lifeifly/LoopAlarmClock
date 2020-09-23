package com.example.loopalarmclock.bean;

import java.io.Serializable;

public class LoopAlarmBean implements Serializable {
    private int year,month,day,hour,minute,dd,dh,dm,vabrate,enable,tag,time,times,interval;
    private String type,week;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String label;
    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public LoopAlarmBean(String type,String week,int tag, int year, int month, int day, int hour, int minute, int dd, int dh, int dm, int vabrate, String label, int enable,int time,int times,int interval) {
        this.tag=tag;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.dd = dd;
        this.dh = dh;
        this.dm = dm;
        this.vabrate = vabrate;
        this.label = label;
        this.enable=enable;
        this.time=time;
        this.times=times;
        this.interval=interval;
        this.type=type;
        this.week=week;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public int getDh() {
        return dh;
    }

    public void setDh(int dh) {
        this.dh = dh;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getVabrate() {
        return vabrate;
    }

    public void setVabrate(int vabrate) {
        this.vabrate = vabrate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
