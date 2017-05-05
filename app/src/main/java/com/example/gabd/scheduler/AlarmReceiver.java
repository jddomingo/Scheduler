package com.example.gabd.scheduler;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
    @Override
    public void onReceive(final Context context, Intent intent) {
        String str = "woah";
        String act_name = intent.getStringExtra("name");
        NotificationManager nmm =   (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder (context)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle("Activity")
                .setContentText("Alarm " + act_name + " went off!");
        Log.e(str, str);
        //Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        //context.startService(serviceIntent);
        miBand = new MiBand(context);
        Object[] devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
        final BluetoothDevice device = (BluetoothDevice) devices[0];
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
