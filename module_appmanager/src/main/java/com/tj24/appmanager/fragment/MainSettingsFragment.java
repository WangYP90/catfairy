package com.tj24.appmanager.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;
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
        permissionpreference.setShouldDisableView(true);
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
    RecyclerView rv;
    ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int count = container.getChildCount();
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
        super.setDivider(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void setDividerHeight(int height) {
        super.setDividerHeight(0);
    }
}
