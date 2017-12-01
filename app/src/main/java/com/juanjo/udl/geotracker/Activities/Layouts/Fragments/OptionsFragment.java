package com.juanjo.udl.geotracker.Activities.Layouts.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.juanjo.udl.geotracker.R;

public class OptionsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.options_activity);
    }//onCreate
}