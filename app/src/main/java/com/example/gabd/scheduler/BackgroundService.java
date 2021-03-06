package com.example.gabd.scheduler;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import static java.lang.Math.abs;

/**
 * Created by Jose Gabriel Domingo on 2/13/17.
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

    //1000 = 1milisecond
    final int CALENDAR_MS = 1000;
    final int CALENDAR_MIN = CALENDAR_MS * 60;
    final int CALENDAR_HOUR = CALENDAR_MIN * 60;
    final int CALENDAR_DAY = CALENDAR_HOUR * 24;
    final int CALENDAR_WEEK = CALENDAR_DAY * 7;
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

        if (intent.getStringExtra("string").equalsIgnoreCase("repeat")) {
            final int hour = intent.getIntExtra("hour", 0);
            final int minute = intent.getIntExtra("minute", 0);
            int interval = intent.getIntExtra("interval", 0);
            final int id = intent.getIntExtra("_id", 0);
            final int repeat = intent.getIntExtra("repeat", 1);
            final Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
            final int date = intent.getIntExtra("date", 0);
            final int chose = intent.getIntExtra("chose", 1);
            final int[] days = intent.getIntArrayExtra("days");
            long actual = intent.getLongExtra("actual", 0);


            //Creates intent with alarm info to send back to AlarmReceiver
            Intent myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra("hour", hour);
            myIntent.putExtra("alarm", alarm);
            myIntent.putExtra("minute", minute);
            myIntent.putExtra("intval", interval);
            myIntent.putExtra("_id", id);
            myIntent.putExtra("chose", chose);
            myIntent.putExtra("date", date);
            myIntent.putExtra("prev", intent.getLongExtra("prev", 0));
            myIntent.putExtra("repeat", repeat + 1);
            myIntent.putExtra("days", intent.getIntArrayExtra("days"));



            Log.e("chose", String.valueOf(chose));

            Calendar cal = Calendar.getInstance();
            //Interval and calendar is set dependent on Interval Type
            if (chose == 1) {
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                if (interval == CALENDAR_DAY) {
                    cal.set(Calendar.HOUR, hour);
                }
                if (interval == CALENDAR_WEEK) {
                    cal.set(Calendar.DAY_OF_WEEK, date);
                }
            } else {
                int today = cal.get(Calendar.DAY_OF_WEEK);
                int i = today%7;
                while (days[i] != 1) {
                    i++;
                    if (i == 7) i = 0;
                }
                Log.e("repeattoday", String.valueOf(today));
                Log.e("repeati", String.valueOf(i));
                if (i < today) i = i + 7;
                interval = (abs(i-today)+1) * CALENDAR_DAY;
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.HOUR, hour);
            }
            actual = (long)(actual + interval);
            myIntent.putExtra("actual", (long)actual);


            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.SECOND, 0);
            //Creates a pending intent called by captured by Broadcast Receivers. Contains info about which receiver it is for
            pending_intent = PendingIntent.getBroadcast(BackgroundService.this, id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //Creates a system alarm that sends an intent to the AlarmReceiver
            alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm_manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 5000, pending_intent);

        } else {
            String[] days_of_the_week = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            Log.e("BackgroundService", "Service running");

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
            int chose = intent.getIntExtra("CHOSE", 1);
            String str_min = intent.getStringExtra("MINUTE");
            String str_hour = intent.getStringExtra("HOUR");
            String act_name = intent.getStringExtra("NAME");
            String interval = intent.getStringExtra("INTERVAL");
            int[] days = intent.getIntArrayExtra("days");

            for (int j = 0; j < days.length; j++) {
                if (days[j] == 1) {
                    Log.e("Checked", String.valueOf(j));
                }
                Log.e("NoCheck", String.valueOf(days[j]));
            }

            Log.e("BackgroundService", "" + intent.getStringExtra("NAME"));

            //Gets interval of alarm
            minute = Integer.parseInt(str_min);
            int hour = Integer.parseInt(str_hour);
            String valinter = new String("Once");
            int intval = 0;
            if (chose == 1) {
                if (interval.equalsIgnoreCase("daily")) {
                    intval = CALENDAR_DAY;
                    valinter = "Daily";
                } else if (interval.equalsIgnoreCase("weekly")) {
                    intval = CALENDAR_WEEK;
                    valinter = "Weekly";
                } else if (interval.equalsIgnoreCase("hourly")) {
                    intval = CALENDAR_HOUR;
                    valinter = "Hourly";
                }
            } else {
                valinter = "";
                intval = CALENDAR_DAY;
                for (int i = 0; i < 7; i++) {
                    if (days[i] == 1) {
                        valinter = valinter + days_of_the_week[i];
                    }
                }
            }

            //Converts time to HH:MM format
            if (minute < 10) str_min = "0" + String.valueOf(minute);

            String time = new String();
            time = str_hour + ":" + str_min;

            //Sets id of pending intent to current time (for uniqueness)
            final Calendar calendar = Calendar.getInstance();
            final int _id = (int) calendar.getTimeInMillis();
            final int date = calendar.get(Calendar.DAY_OF_WEEK);

            //Creates alarm object and is stored in a database
            Alarm newalarm = new Alarm(_id, act_name, time, valinter, 0, 0, days, date, chose, hour, minute, intval);
            alarmlist.add(newalarm);
            tdb.putListObject(listalarm, alarmlist);
            myIntent.putExtra("name", act_name);
            newalarm = (Alarm) alarmlist.get(alarmlist.size() - 1);
            myIntent.putExtra("alarm", newalarm);
            myIntent.putExtra("hour", hour);
            myIntent.putExtra("chose", chose);
            myIntent.putExtra("minute", minute);
            myIntent.putExtra("intval", intval);
            myIntent.putExtra("_id", _id);
            myIntent.putExtra("days", days);
            myIntent.putExtra("date", date);
            myIntent.putExtra("repeat", 0);

            //Sets the calendar to the given time
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            myIntent.putExtra("actual", (long)(calendar.getTimeInMillis()+1000));

            //Creates a pending intent called by captured by Broadcast Receivers. Contains info about which receiver it is for
            pending_intent = PendingIntent.getBroadcast(BackgroundService.this, _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Creates a system alarm that sends an intent to the AlarmReceiver
            alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 200, pending_intent);
        }
    }
}
