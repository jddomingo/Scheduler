package com.example.gabd.scheduler;
import com.example.gabd.scheduler.Alarm;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recView;
    private List<Alarm> alarms;
    AlarmManager alarm_manager;
    TinyDB tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tdb = new TinyDB(this);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        recView = (RecyclerView) findViewById(R.id.recview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recView.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void initializeData(){
        alarms = new ArrayList<>();
        ArrayList<Object> alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        for (int i = 0; i < alarmlist.size(); i++) {
            alarms.add((Alarm) alarmlist.get(i));
        }
    }

    private void initializeAdapter(){
        final MyAdapter adapter = new MyAdapter(alarms);
        recView.setAdapter(adapter);
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            PendingIntent pending_intent;
                            ArrayList<Object> alarmlist = tdb.getListObject("alarmlist", Alarm.class);
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    alarms.remove(position);
                                    Alarm delete_alarm = (Alarm) alarmlist.get(position);
                                    Intent myIntent = new Intent(ListActivity.this, AlarmReceiver.class);
                                    pending_intent = PendingIntent.getBroadcast(ListActivity.this, (int)delete_alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    Log.e("swipe", String.valueOf((int)delete_alarm.getId()));
                                    pending_intent.cancel();
                                    alarm_manager.cancel(pending_intent);
                                    alarmlist.remove(position);
                                    tdb.putListObject("alarmlist", alarmlist);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    alarms.remove(position);
                                    Alarm delete_alarm = (Alarm) alarmlist.get(position);
                                    Intent myIntent = new Intent(ListActivity.this, AlarmReceiver.class);
                                    pending_intent = PendingIntent.getBroadcast(ListActivity.this, (int)delete_alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    Log.e("swipe", String.valueOf((int)delete_alarm.getId()));
                                    pending_intent.cancel();
                                    alarmlist.remove(position);
                                    tdb.putListObject("alarmlist", alarmlist);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        recView.addOnItemTouchListener(swipeTouchListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_list, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
