package com.tj24.appmanager.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.tj24.appmanager.R;

public class MainSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_main_preference);
    }
}
