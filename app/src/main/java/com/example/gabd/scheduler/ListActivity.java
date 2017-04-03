package com.example.gabd.scheduler;
import com.example.gabd.scheduler.Alarm;

import android.app.Activity;
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

        recView = (RecyclerView) findViewById(R.id.recview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recView.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

    }

    private void initializeData(){
        alarms = new ArrayList<>();
        tdb = new TinyDB(this);
        ArrayList<Object> alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        for (int i = 0; i < alarmlist.size(); i++) {
            alarms.add((Alarm) alarmlist.get(i));
        }
    }

    private void initializeAdapter(){
        MyAdapter adapter = new MyAdapter(alarms);
        recView.setAdapter(adapter);
    }
}
