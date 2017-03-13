package com.example.gabd.scheduler;

/**
 * Created by gabd on 3/9/17.
 */

public class Alarm {
    String hour;
    String min;
    String name;

    Alarm(String name, String hour, String min) {
        this.hour = hour;
        this.min = min;
        this.name = name;
    }
}
