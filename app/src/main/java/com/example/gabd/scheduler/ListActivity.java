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
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recView;
    private List<Alarm> alarms;
    AlarmManager alarm_manager;
    TinyDB tdb;

    /**
     * Lists all alarms represented by Cards.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_list);
        onNavigationButtonSelect();

        tdb = new TinyDB(this);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        recView = (RecyclerView) findViewById(R.id.recview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recView.setLayoutManager(llm);


        initializeData();
        initializeAdapter();

    }


    /**
     * Sets list of alarms from the database
     */
    private void initializeData(){
        alarms = new ArrayList<>();
        ArrayList<Object> alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        for (int i = 0; i < alarmlist.size(); i++) {
            alarms.add((Alarm) alarmlist.get(i));
        }
    }


    /**
     * Creates card from set of alarms passed to initializeData()
     */
    private void initializeAdapter(){

        //Creates an adapter for alarms which fills a Recycler View with Card Views of alarms
        final MyAdapter adapter = new MyAdapter(alarms);
        recView.setAdapter(adapter);

        //Cancels alarm by either swiping cards left or right
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
                                    //removes swiped alarm from database
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

    /**
     * Adds listeners to buttons for navigation. Allows navigation between screens
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
}
