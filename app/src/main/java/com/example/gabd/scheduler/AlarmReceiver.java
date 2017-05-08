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
        String str = "woah";
        String act_name = intent.getStringExtra("name");
        final Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
        Log.e("Alarm", "wee+ " +String.valueOf(alarm.getDonecount()));
        NotificationManager nmm =   (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder (context)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle("Activity")
                .setContentText("Alarm " + act_name + " went off!");

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(serviceIntent);

        Intent dialogIntent = new Intent(context, ConfirmActivity.class);
        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TinyDB tdb = new TinyDB(context);
        tdb.putObject("curralarm", alarm);
        context.startActivity(dialogIntent);
        BluetoothAdapter mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueToothAdapter.isEnabled()){
            Object[] devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
            for (int i = 0; i < devices.length; i++) {
                device = (BluetoothDevice) devices[i];
                if (String.valueOf(device.getClass()).equalsIgnoreCase("class android.bluetooth.BluetoothDevice") && device.getName().equalsIgnoreCase("MI")) {
                    break;
                }
            }
            miBand = new MiBand(context);
            nmm.notify(1, ncb.build());

            int mnm = 1;


            miBand.connect(device, new ActionCallback() {
                @Override
                public void onSuccess(Object data) {
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
