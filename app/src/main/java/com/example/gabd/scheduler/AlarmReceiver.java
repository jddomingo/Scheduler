package com.example.gabd.scheduler;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by gabd on 2/8/17.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private MiBand miBand;
    BluetoothDevice device;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final Alarm alarm = (Alarm) intent.getSerializableExtra("alarm"); //Gets instance of current activity to fire

        //Builds a notification and notifies user through the notification bar
        NotificationManager nmm =   (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder (context)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle("Activity")
                .setContentText("Alarm " + alarm.name + " went off!");
        nmm.notify(0, ncb.build());

        //Calls a mediaplayer to play a ringtone through phone
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(serviceIntent);

        //Shows a dialog prompting user to confirm activity
        Intent dialogIntent = new Intent(context, ConfirmActivity.class);
        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TinyDB tdb = new TinyDB(context);
        tdb.putObject("curralarm", alarm);
        context.startActivity(dialogIntent);


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
