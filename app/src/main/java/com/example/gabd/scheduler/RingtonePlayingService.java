package com.example.gabd.scheduler;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.widget.Toast;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.model.VibrationMode;

/**
 * Created by gabd on 2/8/17.
 */
public class RingtonePlayingService extends Service{

    private boolean running;
    private Context context;
    MediaPlayer media_player;
    private int sid;
    private MiBand miBand;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(Intent intent, int flags, int sid){
        final int MAX_VOLUME =  100;
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 80)/Math.log(MAX_VOLUME)));
        Log.e("RingTone", "we here");

        Intent i_1 = new Intent(this.getApplicationContext(), BackgroundService.class);
        PendingIntent pending_intent = PendingIntent.getActivity(this, 0, i_1, 0);

        media_player = MediaPlayer.create(this, R.raw.alarm);
        media_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        media_player.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Log.e("on Destroy called", "ta da");

        super.onDestroy();
        this.running = false;
    }

}
