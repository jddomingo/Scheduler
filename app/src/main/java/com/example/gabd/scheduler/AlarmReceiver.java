package com.example.gabd.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by gabd on 2/8/17.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String str = "woah";
        Log.e(str, str);
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(serviceIntent);
    }
}
