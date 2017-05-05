package com.example.gabd.scheduler;

import java.io.Serializable;

/**
 * Created by gabd on 3/9/17.
 */
@SuppressWarnings("serial")
public class Alarm implements Serializable{
    String time;
    String name;
    String interval;
    int donecount;
    int alarmcount;
    int id;

    public Alarm(int id, String name, String time, String interval, int donecount, int alarmcount) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.interval = interval;
        this.donecount = donecount;
        this.alarmcount = alarmcount;
    }

    public int getId () {
        return id;
    }

    public int getDonecount () { return donecount; }

    public int getAlarmcount () { return alarmcount; }

    public void setDonecount(int donecount){
        this.donecount = donecount;
    }

    public void setAlarmcount(int alarmcount){
        this.alarmcount = alarmcount;
    }

}
