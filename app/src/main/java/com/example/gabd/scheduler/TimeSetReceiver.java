package com.example.gabd.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class TimeSetReceiver extends BroadcastReceiver {
    TinyDB tdb;
    List<Object> alarmlist;
    int hour;
    int minute;
    int interval;
    int id;
    int repeat;
    Alarm alarm;
    int date;

    public TimeSetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        tdb = new TinyDB(context);
        alarmlist = tdb.getListObject("alarmlist", Alarm.class);

        for (int i = 0; i < alarmlist.size(); i++) {
            hour = alarmlist.get(i).
            al int hour = intent.getIntExtra("hour", 0);
            final int minute = intent.getIntExtra("minute", 0);
            final int interval = intent.getIntExtra("interval", 0);
            final int id = intent.getIntExtra("_id", 0);
            final int repeat = intent.getIntExtra("repeat", 1);
            final Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
            final int date = intent.getIntExtra("date", 0);
        }*/
    }
}
