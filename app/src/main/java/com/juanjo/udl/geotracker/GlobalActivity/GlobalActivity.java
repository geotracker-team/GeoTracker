package com.juanjo.udl.geotracker.GlobalActivity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by DejanLojpur on 11/20/2017.
 */

public class GlobalActivity extends AppCompatActivity {
    protected void processException (final Exception e)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
