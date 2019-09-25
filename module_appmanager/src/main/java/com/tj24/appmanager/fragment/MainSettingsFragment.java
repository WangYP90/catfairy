package com.tj24.appmanager.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.tj24.appmanager.R;
import com.tj24.base.utils.Sputil;

import java.util.HashSet;

public class MainSettingsFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "MainSettingsFragment";

    SwitchPreferenceCompat spSwitchHideApp;
    Preference spLookOtherUse;
    MultiSelectListPreference spListCustomOrder;
    SwitchPreferenceCompat spSwitchAutoUpdate;
    Preference spUpdate;
    Preference spAbout;
    Preference spUserAgreement;
    Preference spAppInfo;
    Preference spUpdatePwd;
    Preference spLoginout;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_main_preference);
        initPreference();
    }

    private void initPreference() {
        spSwitchHideApp = (SwitchPreferenceCompat) findPreference(getString(R.string.app_sp_hide_cant_open_app));
        spLookOtherUse = findPreference(getString(R.string.app_sp_permission_other_use));
        spListCustomOrder = (MultiSelectListPreference) findPreference(getString(R.string.app_sp_custom_order));
        spSwitchAutoUpdate = (SwitchPreferenceCompat) findPreference(getString(R.string.app_sp_auto_check_update));
        spUpdate = findPreference(getString(R.string.app_sp_check_update));
        spAbout = findPreference(getString(R.string.app_sp_about));
        spUserAgreement = findPreference(getString(R.string.app_sp_user_agreenment));
        spAppInfo = findPreference(getString(R.string.app_sp_app_info));
        spUpdatePwd = findPreference(getString(R.string.app_sp_reset_pwd));
        spLoginout = findPreference(getString(R.string.app_sp_login_out));

        spLookOtherUse.setOnPreferenceClickListener(this);
        spUpdate.setOnPreferenceClickListener(this);
        spAbout.setOnPreferenceClickListener(this);
        spUserAgreement.setOnPreferenceClickListener(this);
        spAppInfo.setOnPreferenceClickListener(this);
        spUpdatePwd.setOnPreferenceClickListener(this);
        spLoginout.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setEntries(){

        spListCustomOrder.setEntries(R.array.app_entries_orders);
        spListCustomOrder.setEntryValues(R.array.app_entries_orders_values);

        spListCustomOrder.setEntries(R.array.app_entries_orders_no_usetime);
        spListCustomOrder.setEntryValues(R.array.app_entries_orders_values_no_usetime);

        Sputil.read("app_sp_custom_order",new HashSet<String>());
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(spLookOtherUse.getKey())){

        }else if(preference.getKey().equals(spUpdate.getKey())){

        }else if(preference.getKey().equals(spAbout.getKey())){

        }else if(preference.getKey().equals(spUserAgreement.getKey())){

        }else if(preference.getKey().equals(spAppInfo.getKey())){

        }else if(preference.getKey().equals(spUpdatePwd.getKey())){

        }else if(preference.getKey().equals(spLoginout.getKey())){

        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(spSwitchHideApp.getKey())){

        }else if(key.equals(spSwitchAutoUpdate.getKey())){

        }else if(key.equals(spListCustomOrder.getKey())){

        }
    }
}
