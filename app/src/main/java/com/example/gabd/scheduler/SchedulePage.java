package com.example.gabd.scheduler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Calendar;

import static android.R.attr.entries;

public class SchedulePage extends AppCompatActivity {
    AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private TimePicker time_picker;
    private EditText edit_text;
    private RadioButton daily;
    private RadioButton hourly;
    private RadioButton weekly;
    private RadioButton once;
    private MultiSpinner multiSpinner;
    private static SchedulePage inst;
    private AlarmReceiver alarm;
    private Context context;
    TinyDB tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_schedule);
        onNavigationButtonSelect();

        this.context = this;

        final Calendar calendar = Calendar.getInstance();

        final Intent myIntent = new Intent(this.context, BackgroundService.class);

        final Bundle bundle = new Bundle();

        multiSpinner = (MultiSpinner) findViewById(R.id.multispinner);
        Log.e("Spinner", String.valueOf(multiSpinner.getSelectedItem()));
        final boolean[] selected = multiSpinner.getSelected();
        CharSequence[] entrys = multiSpinner.getEntries();

        daily = (RadioButton) findViewById(R.id.daily);
        weekly = (RadioButton) findViewById(R.id.weekly);
        hourly = (RadioButton) findViewById(R.id.hourly);
        once = (RadioButton) findViewById(R.id.once);
        daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weekly.setChecked(false);
                    hourly.setChecked(false);
                    once.setChecked(false);
                }
            }
        });
        weekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(false);
                    hourly.setChecked(false);
                    once.setChecked(false);
                }
            }
        });
        hourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(false);
                    weekly.setChecked(false);
                    once.setChecked(false);
                }
            }
        });
        once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(false);
                    weekly.setChecked(false);
                    hourly.setChecked(false);
                }
            }
        });

        Button alarm_start = (Button) findViewById(R.id.fab);
        edit_text = (EditText) findViewById(R.id.editText);
        edit_text.bringToFront();
        alarm_start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                time_picker = (TimePicker) findViewById(R.id.timePicker);
                int[] days_array = {0, 0, 0, 0, 0, 0, 0};
                for (int i = 0; i < 7; i++) {
                    if (true == selected[i]) {
                        days_array[i] = 1;
                    }
                }

                final int hour = time_picker.getHour();
                final int minute = time_picker.getMinute();
                final Editable act_name = edit_text.getText();
                String interval = new String("once");
                if (daily.isChecked()) { interval = "daily"; }
                if (weekly.isChecked()) { interval = "weekly"; }
                if (hourly.isChecked()) { interval = "hourly"; }


                bundle.putString("HOUR", String.valueOf(hour));
                bundle.putString("MINUTE", String.valueOf(minute));
                bundle.putString("NAME", String.valueOf(act_name));
                bundle.putString("INTERVAL", interval);
                bundle.putIntArray("DAYS", days_array);
                myIntent.putExtras(bundle);

                startService(myIntent);

                Toast.makeText(v.getContext(), "Alarm added!", Toast.LENGTH_SHORT).show();

            }
        });

    }


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

}
