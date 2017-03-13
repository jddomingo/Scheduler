package com.example.gabd.scheduler;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gabd on 3/9/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.AlarmViewHolder> {
    List<Alarm> alarms;

    public class AlarmViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView alarmname;
        TextView alarmhour;
        TextView alarmmin;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            alarmname = (TextView) itemView.findViewById(R.id.alarm_name);
            alarmhour = (TextView) itemView.findViewById(R.id.alarm_hour);
            alarmmin = (TextView) itemView.findViewById(R.id.alarm_min);
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
        holder.alarmhour.setText(alarms.get(position).hour);
        holder.alarmmin.setText(alarms.get(position).min);
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
