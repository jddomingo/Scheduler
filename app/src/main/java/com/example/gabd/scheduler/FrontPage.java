package com.example.gabd.scheduler;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.model.VibrationMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Pattern;

public class FrontPage extends AppCompatActivity {
    private MiBand miband;
    private BluetoothAdapter mBA;
    private int connected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);
        onNavigationButtonSelect();

        final TinyDB tdb = new TinyDB(this); //Creates instance of alarm database

        miband = new MiBand(this); //Creates instance of Mi Band

        //Creates button handlers
        final Button mi_connect = (Button) findViewById(R.id.button2);
        Button start = (Button) findViewById(R.id.start);
        Button export = (Button) findViewById(R.id.export);

        //Creates a Bluetooth Adapter
        mBA = BluetoothAdapter.getDefaultAdapter();

        //Sets listener. Connects with Mi Band device
        mi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Checks first if Bluetooth is on or is supported by device
                if(mBA.isEnabled()) {
                    Object[] devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray();
                    final BluetoothDevice device = (BluetoothDevice) devices[0];
                    miband = new MiBand(v.getContext());

                    if (connected == 0) {
                        connected = 1;
                        mi_connect.setText("Disconnect mi band");
                        miband.connect(device, new ActionCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                Log.d("FPage", "Connected with Mi Band!" + device.getAddress() + device.getName()
                                        + String.valueOf(device.getType())
                                        + String.valueOf(device.getClass()));
                                //show SnackBar/Toast or something
                            }

                            @Override
                            public void onFail(int errorCode, String msg) {
                                Log.d("Fpage", "Connection failed: " + msg);
                            }
                        });
                    } else {
                        connected = 0;
                        miband = null;
                        mi_connect.setText("connect mi band");
                    }
                } else {
                    Toast.makeText(v.getContext(), "Turn on Bluetooth!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Set listener for Mi Band search. Checks if Mi Band connection was successful by vibration
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (miband != null) {
                    miband.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);
                } else {
                    Toast.makeText(v.getContext(), "No Mi Band Connected", Toast.LENGTH_SHORT);
                }
            }
        });

        //Generates a report and writes to a text file stored in the mobile device's external storage
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity host = (Activity) v.getContext();
                v.isFocused();
                verifyStoragePermissions(host);
                File myPath = new File(Environment.getExternalStorageDirectory().toString());
                File myFile = new File(myPath, "Alarm Report.txt");
                FileWriter fw = null;
                try {
                    fw = new FileWriter(myFile);
                    PrintWriter pw = new PrintWriter(fw);
                    String test = new String(" ");
                    String[] array = new String[0];
                    String[] arr2 = new String[0];
                    String[] arr3 = new String[0];
                    Map<String,?> pMap = tdb.getAll();
                    for(Map.Entry<String, ?> entry: pMap.entrySet()) {
                        test = entry.getValue().toString();
                        array = test.split("‚‗‚");
                    }
                    pw.println("Count,Interval,Name,Time");
                    for (int i = 0; i < array.length; i++) {
                        arr2 = array[i].split(":");
                        arr3 = arr2[2].split(",");
                        pw.print(arr3[0] + "/");
                        arr3 = arr2[1].split(",");
                        pw.print(arr3[0] + ",");
                        for (int j = 4; j < arr2.length-2; j++) {
                            arr3 = arr2[j].split(",");
                            pw.print(arr3[0] + ",");
                        }
                        arr3 = arr2[arr2.length-2].split(",");
                        pw.print(arr3[0].replaceAll("\\}", "") + ":");
                        arr3 = arr2[arr2.length-1].split(" ");
                        pw.println(arr3[0].replaceAll("\\}", ""));
                    }
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Adds listeners to buttons. Allows navigation between screens
     */
    private void onNavigationButtonSelect() {
        ImageButton add = (ImageButton) findViewById(R.id.addalarm);
        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton list = (ImageButton) findViewById(R.id.listsched);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SchedulePage.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FrontPage.class);
                startActivity(intent);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListActivity.class);
                startActivity(intent);

            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int ACCESS_LOCATION = 1;
    private static String[] ACCESS_STORATE = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
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
            ActivityCompat.requestPermissions(
                    activity,
                    ACCESS_STORATE,
                    ACCESS_LOCATION
            );
        }
    }
}
