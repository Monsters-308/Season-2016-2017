package com.codeteddy.frcscout.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;


import com.codeteddy.frcscout.R;
import com.github.machinarius.preferencefragment.PreferenceFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
