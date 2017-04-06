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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SchedulePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private TimePicker time_picker;
    private EditText edit_text;
    private ToggleButton daily;
    private ToggleButton hourly;
    private ToggleButton weekly;
    private static SchedulePage inst;
    private AlarmReceiver alarm;
    private Context context;
    TinyDB tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        this.context = this;

        final Calendar calendar = Calendar.getInstance();

        final Intent myIntent = new Intent(this.context, BackgroundService.class);

        final Bundle bundle = new Bundle();

        daily = (ToggleButton) findViewById(R.id.daily);
        weekly = (ToggleButton) findViewById(R.id.weekly);
        hourly = (ToggleButton) findViewById(R.id.hourly);
        daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weekly.setChecked(false);
                    hourly.setChecked(false);
                }
            }
        });
        weekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(false);
                    hourly.setChecked(false);
                }
            }
        });
        hourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(false);
                    weekly.setChecked(false);
                }
            }
        });

        Button alarm_start = (Button) findViewById(R.id.fab);
        alarm_start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                time_picker = (TimePicker) findViewById(R.id.timePicker);
                edit_text = (EditText) findViewById(R.id.editText);

                final int hour = time_picker.getHour();
                final int minute = time_picker.getMinute();
                final Editable act_name = edit_text.getText();
                String interval = new String();
                if (daily.isChecked()) { interval = "daily"; }
                if (weekly.isChecked()) { interval = "weekly"; }
                if (hourly.isChecked()) { interval = "hourly"; }


                bundle.putString("HOUR", String.valueOf(hour));
                bundle.putString("MINUTE", String.valueOf(minute));
                bundle.putString("NAME", String.valueOf(act_name));
                bundle.putString("INTERVAL", interval);
                myIntent.putExtras(bundle);

                startService(myIntent);

                Toast.makeText(v.getContext(), "Alarm added!", Toast.LENGTH_SHORT).show();

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_schedule_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_star) {
            Intent intent = new Intent(this, Achievements.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_today) {
            Intent intent = new Intent(this, SchedulePage.class);
            startActivity(intent);
        } else if (id == R.id.list) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
