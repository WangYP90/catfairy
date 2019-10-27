package com.tj24.appmanager.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.tj24.appmanager.R;
import com.tj24.appmanager.model.CloudModel;

public class CloudSettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    public static final String TAG = "CloudSettingsFragment";
    private Context mContext;

    SwitchPreferenceCompat spSwitchAutoUpload;
    Preference spUpload;
    Preference spDownLoad;
    CloudModel cloudModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_cloud_preference);
        initPreference();
        cloudModel = new CloudModel(mContext);
    }

    private void initPreference() {
        spSwitchAutoUpload = (SwitchPreferenceCompat) findPreference(getString(R.string.app_sp_auto_upload));
        spUpload = findPreference(getString(R.string.app_sp_upload));
        spDownLoad = findPreference(getString(R.string.app_sp_download));

        spUpload.setOnPreferenceClickListener(this);
        spDownLoad.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(getString(R.string.app_sp_upload))){
            cloudModel.readyPush(false);
        }else if(preference.getKey().equals(getString(R.string.app_sp_download))){
            cloudModel.readyPull();
        }
        return false;
    }
}
