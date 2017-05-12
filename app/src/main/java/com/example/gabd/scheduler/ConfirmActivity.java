package com.example.gabd.scheduler;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

/**
 * Created by gabd on 5/7/17.
 */

public class ConfirmActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAlertDialog();
    }


    /**
     * Creates a Dialog Fragment. Asks user if activity was done or not.
     */
    private void showAlertDialog() {
        FragmentManager fm = getFragmentManager();
        DetailsFragment details = new DetailsFragment();
        details.show(fm, "Activity Confirmation");
    }

}
