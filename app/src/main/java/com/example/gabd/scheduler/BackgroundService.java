package com.example.gabd.scheduler;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

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
    public BackgroundService bgservice;

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

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String str_min = intent.getStringExtra("MINUTE");
        Log.e("BackgroundService", ""+intent.getStringExtra("HOUR"));
        String str_hour = intent.getStringExtra("HOUR");

        int minute;
        minute = Integer.parseInt(str_min);
        int hour = Integer.parseInt(str_hour);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);


        if (minute < 10) str_min = "0" + String.valueOf(minute);
        if (hour > 12) str_hour = String.valueOf(hour - 12);

        pending_intent = PendingIntent.getBroadcast(BackgroundService.this, 12345, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("wew", String.valueOf(calendar.getTimeInMillis()));

        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
    }
}
