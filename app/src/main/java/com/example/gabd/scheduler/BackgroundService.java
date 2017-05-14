package com.example.gabd.scheduler;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by gabd on 2/13/17.
 */
public class BackgroundService extends IntentService {

    AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private TimePicker time_picker;
    private static SchedulePage inst;
    private AlarmReceiver alarm;
    private Context context;
    TinyDB tdb;
    int minute;
    public BackgroundService() {
        super("Background");
    }

    /**
     * Handles construction of alarm and sets of pending intents as alarms
     *
     * @param intent Contains information about the alarm (e.g. name, time, interval)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("BackgroundService", "Service running");

        final int CALENDAR_MS = 1000;
        final int CALENDAR_MIN = CALENDAR_MS * 60;
        final int CALENDAR_HOUR = CALENDAR_MIN * 60;
        final int CALENDAR_DAY = CALENDAR_HOUR * 24;
        final int CALENDAR_WEEK = CALENDAR_DAY * 7;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String listalarm = "alarmlist";
        TinyDB tdb = new TinyDB(this);
        ArrayList<Object> alarmlist = new ArrayList<Object>();
        alarmlist = tdb.getListObject(listalarm, Alarm.class);
        String test = new String(" ");
        String[] array = new String[0];

        //Creates intent
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Gets alarm info from passed intent
        String str_min = intent.getStringExtra("MINUTE");
        String str_hour = intent.getStringExtra("HOUR");
        String act_name = intent.getStringExtra("NAME");
        String interval = intent.getStringExtra("INTERVAL");
        int[] days_array = intent.getIntArrayExtra("DAYS");

        Log.e("BackgroundService", ""+intent.getStringExtra("NAME"));

        //Gets interval of alarm
        minute = Integer.parseInt(str_min);
        int hour = Integer.parseInt(str_hour);
        String valinter = new String("Once");
        int intval = new Integer(0);
        if (interval.equalsIgnoreCase("daily")) { intval = CALENDAR_DAY;
            valinter = "Daily";}
        else if (interval.equalsIgnoreCase("weekly")) {
            intval = CALENDAR_WEEK;
            valinter = "Weekly";
        }
        else if (interval.equalsIgnoreCase("hourly")) { intval = CALENDAR_HOUR;
            valinter = "Hourly";}

        if (minute < 10) str_min = "0" + String.valueOf(minute);

        String time = new String();
        time = str_hour + ":" + str_min;

        final Calendar calendar = Calendar.getInstance();
        final int _id = (int)calendar.getTimeInMillis();

        //Creates alarm object and is stored in a database
        Alarm newalarm  = new Alarm(_id, act_name, time,valinter, 0 ,0);
        alarmlist.add(newalarm);
        tdb.putListObject(listalarm, alarmlist);
        myIntent.putExtra("name", act_name);
        newalarm = (Alarm) alarmlist.get(alarmlist.size()-1);
        myIntent.putExtra("alarm", newalarm);

        //Creates a pending intent called by captured by Broadcast Receivers. Contains info about which receiver it is for
        pending_intent = PendingIntent.getBroadcast(BackgroundService.this, _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Sets the calendar to the given time
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        //Creates a system alarm that sends an intent to the AlarmReceiver
        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intval, pending_intent);
    }
}
