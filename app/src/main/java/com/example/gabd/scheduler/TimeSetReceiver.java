package com.example.gabd.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

public class TimeSetReceiver extends BroadcastReceiver {
    TinyDB tdb;
    public TimeSetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        tdb = new TinyDB(context);
        ArrayList<Object> alarmlist = new ArrayList<Object>();
        alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        Intent myIntent = new Intent(context, BackgroundService.class);
        Alarm alarm = new Alarm();
        for (int i = 0; i < alarmlist.size(); i++) {
            alarm = (Alarm) alarmlist.get(i);
            myIntent.putExtra("string", "repeat");
            myIntent.putExtra("hour", alarm.hour);
            myIntent.putExtra("minute", alarm.minute);
            myIntent.putExtra("days", alarm.days);
            myIntent.putExtra("date", alarm.startdate);
            myIntent.putExtra("_id", alarm.id);
            myIntent.putExtra("interval", alarm.intval);
            myIntent.putExtra("repeat", 1);
            myIntent.putExtra("chose", alarm.chose);
            myIntent.putExtra("alarm", alarm);
            if (alarm.intval > 0) {
                context.startService(myIntent);
            }
        }
    }
}
