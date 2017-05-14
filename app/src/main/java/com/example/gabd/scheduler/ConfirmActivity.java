package com.example.gabd.scheduler;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by gabd on 5/7/17.
 */

public class ConfirmActivity extends Activity {
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);
        FragmentManager fm = getFragmentManager();
        DetailsFragment details = new DetailsFragment();
        details.setArguments(bundle);
        details.show(fm, "Activity Confirmation");
    }

}
