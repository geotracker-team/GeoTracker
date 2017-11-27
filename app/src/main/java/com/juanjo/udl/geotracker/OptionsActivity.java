package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.os.Bundle;

import com.juanjo.udl.geotracker.GlobalActivity.GlobalActivity;

public class OptionsActivity extends GlobalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
    }//onCreate
}
