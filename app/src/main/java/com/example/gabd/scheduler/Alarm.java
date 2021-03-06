package com.example.gabd.scheduler;

import java.io.Serializable;

/**
 * Created by Jose Gabriel Domingo on 3/9/17.
 */
@SuppressWarnings("serial")
public class Alarm implements Serializable{
    int alarmcount; //Counts number of time activity was set off
    int donecount; //Counts number of times activity was done
    int hour;
    int minute;
    String time; //Time alarm is set (hh:mm)
    String name; //Name of Activity set by alarm
    String interval; //Interval at which alarm sets off (e.g every hour, day, week)
    int id; //Unique id of alarm (Usually needed to access corresponding pending_intent)
    int[] days;
    int startdate;
    int chose;
    int intval;

    public Alarm(){

    }

    public Alarm(int id, String name, String time, String interval, int donecount, int alarmcount, int[] days, int startdate, int chose, int hour, int minute, int intval) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.interval = interval;
        this.donecount = donecount;
        this.alarmcount = alarmcount;
        this.days = days;
        this.startdate = startdate;
        this.chose = chose;
        this.hour = hour;
        this.minute = minute;
        this.intval = intval;
    }



    public int getId () {
        return id;
    } //Returns unique id of alarm

    public int getDonecount () { return donecount; } //Returns number of times activity was done

    public int getAlarmcount () { return alarmcount; } //Returns number of times activity was set off

    public void setDonecount(int donecount){
        this.donecount = donecount;
    }

    public void setAlarmcount(int alarmcount){
        this.alarmcount = alarmcount;
    }

}
