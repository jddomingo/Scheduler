package com.example.gabd.scheduler;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by gabd on 3/9/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.AlarmViewHolder> {
    List<Alarm> alarms;
    String[] hold = new String[2];

    public class AlarmViewHolder extends RecyclerView.ViewHolder{
        public View view;
        CardView cv;
        TextView alarmname;
        TextView alarmtime;
        String name = "test";

        public AlarmViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            alarmname = (TextView) itemView.findViewById(R.id.alarm_name);
            alarmtime = (TextView) itemView.findViewById(R.id.alarm_hour);
            view = itemView;
            TinyDB tdb = new TinyDB(view.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = getAdapterPosition();
                    Log.e("Adapter: ", (String) alarmname.getText());
                    PopupMenu popup;
                    popup = new PopupMenu(view.getContext(), view);

                    popup.getMenuInflater().inflate(R.menu.alarm_edit_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("Details")) {
                                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                                adb.setTitle("Activity Details")
                                   .setMessage("Activity Name: " + alarms.get(position).name + "\nTime: " + alarms.get(position).time + "\nInterval: " + alarms.get(position).interval)
                                   .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(view.getContext(), "Closed", Toast.LENGTH_SHORT).show();
                                    }
                                }).create();
                                AlertDialog ad = adb.create();
                                ad.show();
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        }

    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        AlarmViewHolder avh = new AlarmViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        holder.alarmname.setText(alarms.get(position).name);
        holder.alarmtime.setText(alarms.get(position).time);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    MyAdapter(List<Alarm> alarms) {
        this.alarms = alarms;
    }


}
