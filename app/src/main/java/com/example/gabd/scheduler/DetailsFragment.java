package com.example.gabd.scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabd on 5/1/17.
 */

public class DetailsFragment extends DialogFragment {
    Alarm curralarm = new Alarm(0, " ", " ", " ", 0, 0);
    TinyDB tdb;
    ArrayList<Object> alarmlist;
    int j = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        tdb = new TinyDB(getActivity());
        curralarm = (Alarm) tdb.getObject("curralarm", Alarm.class);
        Log.e("Fragment", String.valueOf(curralarm.getId()));
        final Intent frontPageIntent = new Intent(getActivity(), FrontPage.class);
        frontPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);alarmlist = new ArrayList<Object>();
        Alarm a = new Alarm(0, " ", " ", " ", 0, 0);
        alarmlist = tdb.getListObject("alarmlist", Alarm.class);
        for (int i = 0; i < alarmlist.size();i++) {
            a = (Alarm) alarmlist.get(i);
            if (a.getId() == curralarm.getId()) {
                curralarm = a;
                j = i;
                break;
            }
        }

        tdb.remove("curralarm");
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Activity Details")
                .setMessage("Pending Activity!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity Done!", Toast.LENGTH_SHORT).show();
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                        curralarm.setDonecount(curralarm.getDonecount()+1);
                        alarmlist.remove(j);
                        alarmlist.add(j, curralarm);
                        tdb.putListObject("alarmlist", alarmlist);
                        dialog.dismiss();
                        getActivity().startActivity(frontPageIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                        alarmlist.remove(j);
                        alarmlist.add(j, curralarm);
                        tdb.putListObject("alarmlist", alarmlist);
                        dialog.dismiss();
                        getActivity().startActivity(frontPageIntent);
                    }
                }).create();
        return alertDialog;
    }
}
