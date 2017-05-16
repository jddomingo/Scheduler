package com.example.gabd.scheduler;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.model.VibrationMode;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by gabd on 2/8/17.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private MiBand miBand;
    BluetoothDevice device;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("Alarm Receiver", "how");
        final Alarm alarm = (Alarm) intent.getSerializableExtra("alarm"); //Gets instance of current activity to fire
        final int hour = intent.getIntExtra("hour", 0);
        final int minute = intent.getIntExtra("minute", 0);
        final int interval = intent.getIntExtra("intval", 0);
        final int _id = intent.getIntExtra("_id", 0);
        final int date = intent.getIntExtra("date", 0);

        Log.e("Alarm Receiver", String.valueOf(interval) + String.valueOf(hour) + String.valueOf(minute));

        //Sets new alarm depending on interval
        if (interval > 0) {
            int repeat = intent.getIntExtra("repeat", 0);
            Intent receiveIntent = new Intent(context, BackgroundService.class);
            receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            receiveIntent.putExtra("hour", hour);
            receiveIntent.putExtra("minute", minute);
            receiveIntent.putExtra("interval", interval);
            receiveIntent.putExtra("alarm", alarm);
            receiveIntent.putExtra("_id", _id);
            receiveIntent.putExtra("repeat", repeat+1);
            receiveIntent.putExtra("string", "repeat");
            receiveIntent.putExtra("date", date);
            context.startService(receiveIntent);
        }

        //Shows a dialog prompting user to confirm activity
        Intent dialogIntent = new Intent(context, ConfirmActivity.class);
        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("alarm", alarm);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("Alarm Receiver", "dialog");

        //Builds a notification and notifies user through the notification bar
        NotificationManager nmm =   (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder (context)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle("Activity")
                .setContentText("Alarm " + alarm.name + " went off!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        nmm.notify((int) System.currentTimeMillis(), ncb.build());

        Log.e("Alarm Receiver", "notified");
        //Calls a mediaplayer to play a ringtone through phone
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(serviceIntent);

        Log.e("ayy", "wow");

        //Connect to Mi Band
        connectToMiBand(context);
    }

    /**
     * Connects to a paireed Mi Band device
     *
     * @param context Used to get current context of application.
     */
    private void connectToMiBand(Context context) {
        BluetoothAdapter mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueToothAdapter.isEnabled()){

            Object[] devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();//Returns list of devices paired with mobile device
            //Searches paired  devices for Mi Band
            for (int i = 0; i < devices.length; i++) {
                device = (BluetoothDevice) devices[i];
                if (String.valueOf(device.getClass()).equalsIgnoreCase("class android.bluetooth.BluetoothDevice") && device.getName().equalsIgnoreCase("MI")) {
                    break;
                }
            }
            miBand = new MiBand(context);

            //Connects with found device and sets of vibration
            miBand.connect(device, new ActionCallback() {
                @Override
                public void onSuccess(Object data)  {
                    Log.e("Alarm", "Success");
                    miBand.startVibration(VibrationMode.VIBRATION_10_TIMES_WITH_LED);
                    miBand.setDisconnectedListener(new NotifyListener() {
                        @Override
                        public void onNotify(byte[] data) {
                            Log.e("Alarm", "Done");
                        }
                    });
                }

                @Override
                public void onFail(int errorCode, String msg) {
                    Log.e("Alarm", "Fail");
                }
            });
        }
    }
}
