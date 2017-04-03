package com.example.gabd.scheduler;

/**
 * Created by gabd on 3/9/17.
 */

public class Alarm {
    String time;
    String name;
    int id;

    public Alarm(int id, String name, String time) {
        this.id = id;
        this.time = time;
        this.name = name;
    }

}
