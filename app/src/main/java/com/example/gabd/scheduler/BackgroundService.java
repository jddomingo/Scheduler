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
    public static final String MyPREFERENCES = "alarmspref";
    SharedPreferences sharedpreferences;
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
        String idlist = "idlist";
        String hourlist = "hourlist";
        String minutelist = "minutelist";
        String actnames = "actnames";
        ArrayList<Integer> listid = tdb.getListInt(idlist);
        ArrayList<Integer> listhour = tdb.getListInt(hourlist);
        ArrayList<Integer> listmin = tdb.getListInt(minutelist);
        ArrayList<String> activitynames = tdb.getListString(actnames);


        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String str_min = intent.getStringExtra("MINUTE");
        String str_hour = intent.getStringExtra("HOUR");
        String act_name = intent.getStringExtra("NAME");
        Log.e("BackgroundService", ""+intent.getStringExtra("NAME"));

        int minute;
        minute = Integer.parseInt(str_min);
        int hour = Integer.parseInt(str_hour);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);


        if (minute < 10) str_min = "0" + String.valueOf(minute);
        if (hour > 12) str_hour = String.valueOf(hour - 12);

        //editor = sharedPref.edit();
        final int _id = (int)calendar.getTimeInMillis();
        listid.add(_id);
        listhour.add(hour);
        listmin.add(minute);
        activitynames.add(act_name);
        tdb.putListInt(idlist, listid);
        tdb.putListInt(hourlist, listhour);
        tdb.putListInt(minutelist, listmin);
        tdb.putListString(actnames, activitynames);

        pending_intent = PendingIntent.getBroadcast(BackgroundService.this, _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending_intent);
    }
}
