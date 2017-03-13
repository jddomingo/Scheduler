package com.example.gabd.scheduler;
import com.example.gabd.scheduler.Alarm;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {
    private RecyclerView recView;
    private List<Alarm> alarms;


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
        alarms.add(new Alarm("Running", "12:47", " "));
        alarms.add(new Alarm("Baking", "2:32", " "));
        alarms.add(new Alarm("Running", "3:26", " "));
    }

    private void initializeAdapter(){
        MyAdapter adapter = new MyAdapter(alarms);
        recView.setAdapter(adapter);
    }
}
