package com.tj24.appmanager.fragment;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.tj24.appmanager.R;
import com.tj24.base.utils.LogUtil;
import com.tj24.base.utils.Sputil;

import java.util.HashSet;

public class MainSettingsFragment extends PreferenceFragmentCompat {
    public static final String TAG = "MainSettingsFragment";
    Preference permissionpreference;
    MultiSelectListPreference listPreference;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_main_preference);
         permissionpreference = (Preference) findPreference("app_sp_permission_other_use");
         listPreference = (MultiSelectListPreference) findPreference("app_sp_custom_order");
        setEntries();
        permissionpreference.setIconSpaceReserved(false);
        permissionpreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LogUtil.e(TAG,"preference onclick");
                setEntries();
//                boolean b =Sputil.read("app_sp_permission_other_use",false);
//                LogUtil.e(TAG,"b="+b);
                return true;
            }
        });
    }

    public void setEntries(){
            listPreference.setEntries(R.array.app_entries_orders);
            listPreference.setEntryValues(R.array.app_entries_orders_values);

            listPreference.setEntries(R.array.app_entries_orders_no_usetime);
            listPreference.setEntryValues(R.array.app_entries_orders_values_no_usetime);

        Sputil.read("app_sp_custom_order",new HashSet<String>());
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(new ColorDrawable(getResources().getColor(R.color.app_scrim)));
    }

    @Override
    public void setDividerHeight(int height) {
        super.setDividerHeight(5);
    }
}
