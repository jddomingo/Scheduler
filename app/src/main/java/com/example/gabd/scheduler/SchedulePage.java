package com.example.gabd.scheduler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Calendar;

import static android.R.attr.entries;
import static com.example.gabd.scheduler.R.*;
import static com.example.gabd.scheduler.R.drawable.*;

public class SchedulePage extends AppCompatActivity {
    AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private TimePicker time_picker;
    private EditText edit_text;
    private RadioButton daily;
    private RadioButton hourly;
    private RadioButton weekly;
    private ToggleButton mon;
    private ToggleButton tue;
    private ToggleButton wed;
    private ToggleButton thu;
    private ToggleButton fri;
    private ToggleButton sat;
    private ToggleButton sun;
    private RadioButton once;
    private RadioGroup rg;
    public int[] days = {0, 0, 0, 0, 0, 0, 0};
    private MultiSpinner multiSpinner;
    private static SchedulePage inst;
    private AlarmReceiver alarm;
    private int buttonclick;
    private Context context;
    TinyDB tdb;
    final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                final ToggleButton view = (ToggleButton) group.getChildAt(checkedId);
                view.setChecked(view.getId() == checkedId);
            }
        }
    };


    /**
     * Passes user input to BackgroundService for alarm creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.nav_bar_schedule);
        onNavigationButtonSelect();

        rg = (RadioGroup) findViewById(id.toggleGroup);
        rg.setOnCheckedChangeListener(ToggleListener);

        this.context = this;

        //Sets time and date of alarm
        final Calendar calendar = Calendar.getInstance();

        //Intent that stores data of alarm
        final Intent myIntent = new Intent(this.context, BackgroundService.class);

        final Bundle bundle = new Bundle();


        //Input for interval of alarm. Makes sure at most one is checked.


        daily = (RadioButton) findViewById(id.daily);
        weekly = (RadioButton) findViewById(id.weekly);
        hourly = (RadioButton) findViewById(id.hourly);
        once = (RadioButton) findViewById(id.once);

        //Sets OnClickListener for button that passes info
        Button alarm_start = (Button) findViewById(id.fab);
        mon = (ToggleButton) findViewById(id.Monday);
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mon.isChecked()) mon.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else mon.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        tue = (ToggleButton) findViewById(id.Tuesday);
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tue.isChecked()) tue.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else tue.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        wed = (ToggleButton) findViewById(id.Wednesday);
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wed.isChecked()) wed.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else wed.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        thu = (ToggleButton) findViewById(id.Thursday);
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thu.isChecked()) thu.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else thu.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        fri = (ToggleButton) findViewById(id.Friday);
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fri.isChecked()) fri.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else fri.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        sat = (ToggleButton) findViewById(id.Saturday);
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sat.isChecked()) sat.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else sat.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });
        sun = (ToggleButton) findViewById(id.Sunday);
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sun.isChecked()) sun.setBackgroundDrawable(getResources().getDrawable(drawable.buttonclick));
                else sun.setBackgroundDrawable(getResources().getDrawable(drawable.buttonshape));
            }
        });

        //Activity Name Handler
        edit_text = (EditText) findViewById(id.editText);
        edit_text.bringToFront();

        //Interval Option
        final RadioGroup rgInterval = (RadioGroup) findViewById(id.intervalGroup);
        final CheckBox choose = (CheckBox) findViewById(id.choose);
        for (int j = 0; j < rg.getChildCount(); j++) {
            rg.getChildAt(j).setEnabled(false);
        }
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choose.isChecked()) {
                    for (int i = 0; i < rgInterval.getChildCount(); i++) {
                        rgInterval.getChildAt(i).setEnabled(false);
                    }
                    for (int j = 0; j < rg.getChildCount(); j++) {
                        rg.getChildAt(j).setEnabled(true);
                    }
                } else {
                    for (int i = 0; i < rgInterval.getChildCount(); i++) {
                        rgInterval.getChildAt(i).setEnabled(true);
                    }
                    for (int j = 0; j < rg.getChildCount(); j++) {
                        rg.getChildAt(j).setEnabled(false);
                    }
                }
            }
        });
        buttonclick = drawable.buttonclick;
        alarm_start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (mon.isChecked()) days[1] = 1;
                else days[1] = 0;
                if (tue.isChecked()) days[2] = 1;
                else days[2] = 0;
                if (wed.isChecked()) days[3] = 1;
                else days[3] = 0;
                if (thu.isChecked()) days[4] = 1;
                else days[4] = 0;
                if (fri.isChecked()) days[5] = 1;
                else days[5] = 0;
                if (sat.isChecked()) days[6] = 1;
                else days[6] = 0;
                if (sun.isChecked()) days[0] = 1;
                else days[0] = 0;

                time_picker = (TimePicker) findViewById(id.timePicker);

                final int hour = time_picker.getHour();
                final int minute = time_picker.getMinute();
                final Editable act_name = edit_text.getText();
                String interval = new String("once");
                if (!(choose.isChecked())) {
                    if (daily.isChecked()) {
                        interval = "daily";
                    }
                    if (weekly.isChecked()) {
                        interval = "weekly";
                    }
                    if (hourly.isChecked()) {
                        interval = "hourly";
                    }
                    bundle.putInt("CHOSE", 1);
                } else {
                    bundle.putIntArray("days", days);
                    bundle.putInt("CHOSE", 2);
                }

                //Puts Alarm info into a bundle to send
                bundle.putString("HOUR", String.valueOf(hour));
                bundle.putString("INTERVAL", interval);
                bundle.putString("MINUTE", String.valueOf(minute));
                bundle.putString("NAME", String.valueOf(act_name));
                bundle.putString("string", "nope");
                myIntent.putExtras(bundle);

                startService(myIntent);

                Toast.makeText(v.getContext(), "Alarm added!", Toast.LENGTH_SHORT).show();

            }
        });

    }


    /**
     * Adds listeners to buttons. Allows navigation between screens
     */
    private void onNavigationButtonSelect() {
        ImageButton add = (ImageButton) findViewById(id.addalarm);
        ImageButton home = (ImageButton) findViewById(id.home);
        ImageButton list = (ImageButton) findViewById(id.listsched);
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


}
