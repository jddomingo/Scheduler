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
 * Created by Jose Gabriel Domingo on 5/1/17.
 */

public class DetailsFragment extends DialogFragment {
    Alarm curralarm = new Alarm(0, " ", " ", " ", 0, 0);
    TinyDB tdb;
    ArrayList<Object> alarmlist;
    int j = 0;

    /**
     * Creates an Alert Dialog. Prompts user wheter activity was done or not
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Gets alarm currently firing
        tdb = new TinyDB(getActivity());
        curralarm = (Alarm) getArguments().getSerializable("alarm");

        Log.e("Fragment", String.valueOf(curralarm.getId()));

        final Intent frontPageIntent = new Intent(getActivity(), FrontPage.class);
        frontPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmlist = new ArrayList<Object>();

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

        //Creates alert dialog and replaces alarm with new count values
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Remind Mi")
                .setMessage("Was " + curralarm.name + " activity done?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity Done!", Toast.LENGTH_SHORT).show();
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                        curralarm.setDonecount(curralarm.getDonecount()+1);
                        alarmlist.remove(j);
                        alarmlist.add(j, curralarm);
                        tdb.putListObject("alarmlist", alarmlist);
                        dismiss();
                        startActivity(frontPageIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity not done!", Toast.LENGTH_SHORT).show();
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                        alarmlist.remove(j);
                        alarmlist.add(j, curralarm);
                        tdb.putListObject("alarmlist", alarmlist);
                        dismiss();
                        startActivity(frontPageIntent);
                    }
                }).create();
        return alertDialog;
    }
}
