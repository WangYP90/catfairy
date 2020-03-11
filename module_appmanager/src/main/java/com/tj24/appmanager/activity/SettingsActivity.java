package com.tj24.appmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.tj24.appmanager.R;
import com.tj24.appmanager.fragment.CloudSettingsFragment;
import com.tj24.appmanager.fragment.MainSettingsFragment;
import com.tj24.base.base.ui.BaseActivity;

public class SettingsActivity extends BaseActivity {

    private static final String SETTINGS_TYPE = "settingsType";
    public static int SETTINGS_MAIN = 1;
    public static int SETTINGS_CLOUD = 2;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gotoSettings();
    }

    @Override
    public int getLayoutId() {
        return R.layout.app_activity_settings;
    }

    private void gotoSettings() {
        int settingsType = getIntent().getIntExtra(SETTINGS_TYPE, SETTINGS_MAIN);
        if (settingsType == SETTINGS_MAIN) {
            mFragment = new MainSettingsFragment();
        } else if (settingsType == SETTINGS_CLOUD) {
            mFragment = new CloudSettingsFragment();
        }

        if (mFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        } else {
            if (backStackEntryCount > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStartForResult(Activity context, int type, int requestCode) {
        Intent i = new Intent(context, SettingsActivity.class);
        i.putExtra(SETTINGS_TYPE, type);
        context.startActivityForResult(i,requestCode);
    }

    public static void actionStart(Activity context, int type) {
        Intent i = new Intent(context, SettingsActivity.class);
        i.putExtra(SETTINGS_TYPE, type);
        context.startActivity(i);
    }
}
