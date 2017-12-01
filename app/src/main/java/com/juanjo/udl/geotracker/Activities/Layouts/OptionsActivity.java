package com.juanjo.udl.geotracker.Activities.Layouts;

import android.os.Bundle;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalActivity;
import com.juanjo.udl.geotracker.Activities.Layouts.Fragments.OptionsFragment;

public class OptionsActivity extends GlobalActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new OptionsFragment())
                .commit();
    }//onCreate
}
