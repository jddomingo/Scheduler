package com.example.gabd.scheduler;
import com.example.gabd.scheduler.Alarm;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {
    private RecyclerView recView;
    private List<Alarm> alarms;
    TinyDB tdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tdb = new TinyDB(this);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        recView = (RecyclerView) findViewById(R.id.recview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recView.setLayoutManager(llm);

        initializeData();
        initializeAdapter();
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

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            PendingIntent pending_intent;
                            ArrayList<Object> alarmlist = tdb.getListObject("alarmlist", Alarm.class);
                            @Override
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
                                    Intent myIntent = new Intent(recView.getContext(), AlarmReceiver.class);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    pending_intent = PendingIntent.getBroadcast(recView.getContext(), delete_alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                                    Intent myIntent = new Intent(recView.getContext(), AlarmReceiver.class);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    pending_intent = PendingIntent.getBroadcast(recView.getContext(), delete_alarm.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmlist.remove(position);
                                    tdb.putListObject("alarmlist", alarmlist);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        recView.addOnItemTouchListener(swipeTouchListener);
    }

}
