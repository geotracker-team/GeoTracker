package com.juanjo.udl.geotracker.Activities.GlobalActivity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class GlobalActivity extends AppCompatActivity {
    protected void processException (final Exception e)
    {
        showToast(e.getMessage(), Toast.LENGTH_LONG);
    }

    protected void showToast(final String text, final int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, type).show();
            }
        });
    }
}
