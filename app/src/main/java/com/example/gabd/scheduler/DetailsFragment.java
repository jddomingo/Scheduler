package com.example.gabd.scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by gabd on 5/1/17.
 */

public class DetailsFragment extends DialogFragment {
    public static DetailsFragment newInstance(String name, String time){
        DetailsFragment frag = new DetailsFragment();
        Bundle args = new Bundle();
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        String time = getArguments().getString("time");
        final Alarm curralarm = (Alarm) getArguments().getSerializable("alarm");
        return new AlertDialog.Builder(getActivity())
                .setTitle("Activity Details")
                .setMessage("Activity Name: " + name + "\nTime: " + time +"\n")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity Done!", Toast.LENGTH_SHORT).show();
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                        curralarm.setDonecount(curralarm.getDonecount()+1);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curralarm.setAlarmcount(curralarm.getAlarmcount()+1);
                    }
                }).create();
    }
}
    