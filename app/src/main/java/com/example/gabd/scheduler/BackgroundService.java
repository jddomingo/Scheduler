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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    /**
    SharedPreferences sharedPref = context.getSharedPreferences(
            getString(R.string.pref_file_key), Context.MODE_PRIVATE
    );

    SharedPreferences.Editor editor;
     **/

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public BackgroundService() {
        super("Background");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("BackgroundService", "Service running");

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        TinyDB tdb = new TinyDB(this);
        String listalarm = "alarmlist";
        ArrayList<Object> alarmlist = new ArrayList<Object>();
        alarmlist = tdb.getListObject(listalarm, Alarm.class);


        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String str_min = intent.getStringExtra("MINUTE");
        String str_hour = intent.getStringExtra("HOUR");
        String act_name = intent.getStringExtra("NAME");
        String interval = intent.getStringExtra("INTERVAL");
        Log.e("BackgroundService", ""+intent.getStringExtra("NAME"));

        int minute;
        minute = Integer.parseInt(str_min);
        int hour = Integer.parseInt(str_hour);
        int intval = new Integer(0);
        if (interval.equalsIgnoreCase("daily")) { intval = (int) AlarmManager.INTERVAL_DAY; }
        if (interval.equalsIgnoreCase("weekly")) {
            intval = (int) AlarmManager.INTERVAL_DAY;
            intval = intval*7;
        }
        if (interval.equalsIgnoreCase("hourly")) { intval = (int) AlarmManager.INTERVAL_HOUR; }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);


        if (minute < 10) str_min = "0" + String.valueOf(minute);

        String time = new String();
        time = str_hour + ":" + str_min;

        //editor = sharedPref.edit();
        final int _id = (int)calendar.getTimeInMillis();
        alarmlist.add(new Alarm(_id, act_name, time));
        tdb.putListObject(listalarm, alarmlist);

        pending_intent = PendingIntent.getBroadcast(BackgroundService.this, _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intval, pending_intent);
    }
}
