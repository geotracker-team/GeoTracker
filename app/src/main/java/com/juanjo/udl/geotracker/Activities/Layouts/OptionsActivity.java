package com.juanjo.udl.geotracker.Activities.Layouts;

import android.app.Activity;
import android.os.Bundle;

import com.juanjo.udl.geotracker.Activities.Layouts.Fragments.OptionsFragment;

public class OptionsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new OptionsFragment())
                .commit();
    }//onCreate
}
