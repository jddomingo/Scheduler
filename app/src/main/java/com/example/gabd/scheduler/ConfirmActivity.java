package com.example.gabd.scheduler;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Jose Gabriel Domingo on 5/7/17.
 */

public class ConfirmActivity extends FragmentActivity implements DetailsFragment.NoticeDialogListener {
    Alarm curralarm = new Alarm();
    TinyDB tdb;
    ArrayList<Object> alarmlist;
    int j = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        showAlertDialog(alarm);
    }


    /**
     * Creates a Dialog Fragment. Asks user if activity was done or not.
     */
    private void showAlertDialog(Alarm alarm) {
        DialogFragment details = DetailsFragment.newInstance(alarm);
        details.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDialogPositive(DialogFragment dialog) {
        tdb = new TinyDB(this);
        alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        Alarm a = new Alarm();
        Alarm curralarm = (Alarm) dialog.getArguments().getSerializable("alarm");
        for (int i = 0; i < alarmlist.size();i++) {
            a = (Alarm) alarmlist.get(i);
            if (a.getId() == curralarm.getId()) {
                curralarm = a;
                j = i;
                break;
            }
        }
        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
        curralarm.setDonecount(curralarm.getDonecount()+1);
        alarmlist.remove(j);
        alarmlist.add(j, curralarm);
        tdb.putListObject("alarmlist", alarmlist);
        finish();
    }

    @Override
    public void onDialogNegative(DialogFragment dialog) {
        tdb = new TinyDB(this);
        alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        Alarm a = new Alarm();
        Alarm curralarm = (Alarm) dialog.getArguments().getSerializable("alarm");
        for (int i = 0; i < alarmlist.size();i++) {
            a = (Alarm) alarmlist.get(i);
            if (a.getId() == curralarm.getId()) {
                curralarm = a;
                j = i;
                break;
            }
        }
        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
        alarmlist.remove(j);
        alarmlist.add(j, curralarm);
        tdb.putListObject("alarmlist", alarmlist);
        finish();
    }
}
