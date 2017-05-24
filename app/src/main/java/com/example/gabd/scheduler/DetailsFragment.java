package com.example.gabd.scheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jose Gabriel Domingo on 5/1/17.
 */

public class DetailsFragment extends DialogFragment {
    Alarm curralarm = new Alarm();
    TinyDB tdb;
    ArrayList<Object> alarmlist;
    int j = 0;
    Alarm alarm = new Alarm();

    public interface NoticeDialogListener {
        public void onDialogPositive(DialogFragment dialog);
        public void onDialogNegative(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mListener = (NoticeDialogListener) activity;
    }

    /**
     * Creates an Alert Dialog. Prompts user wheter activity was done or not
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        //Gets alarm currently firing
        tdb = new TinyDB(getActivity());

        Log.e("oi", "im lher");
        if (savedInstanceState != null) {
            Log.e("oi", "im back");
            curralarm = (Alarm) savedInstanceState.getSerializable("alarm");
        } else {
            curralarm = (Alarm) getArguments().getSerializable("alarm");
            alarm = curralarm;
        }
        Log.e("Fragment", String.valueOf(curralarm.getId()));

        final Intent frontPageIntent = new Intent(getActivity(), FrontPage.class);
        frontPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmlist = new ArrayList<Object>();

        Alarm a = new Alarm();
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
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Remind Mi")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Was " + curralarm.name + " activity done?    " + String.valueOf(curralarm.getDonecount()) + "/" + String.valueOf(curralarm.getAlarmcount()))
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity Done!", Toast.LENGTH_SHORT).show();
                        mListener.onDialogPositive(DetailsFragment.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Activity not done!", Toast.LENGTH_SHORT).show();
                        mListener.onDialogNegative(DetailsFragment.this);
                    }
                }).create();
        return alertDialog;
    }

    public static DetailsFragment newInstance(Alarm alarm) {
        DetailsFragment frag = new DetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable("alarm", alarm);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("alarm", alarm);
    }
}
