package com.fugao.formula;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.MenuItem;
import android.view.View;

import com.fugao.formula.wifi.AppConfiguration;
import com.fugao.formula.wifi.IConstants;
import com.fugao.formula.wifi.WifiUtils;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private SharedPreferences mSharedPreferences;
    private WifiUtils mWifiUtils;
    private EditTextPreference mName;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initActionBar();
        setupSimplePreferencesScreen();
    }

    /**
     * 设置ActionBar
     */
    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_title);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_back:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        actionBar.getCustomView().findViewById(R.id.ll_back).setOnClickListener(listener);
    }

    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_general);
        innitPreference();
    }

    private void innitPreference() {
        mWifiUtils = new WifiUtils(this);
        mSharedPreferences = getPreferenceScreen().getSharedPreferences();
        bindPreferenceSummaryToValue(findPreference("ip"));
        bindPreferenceSummaryToValue(findPreference("port"));
        mName = (EditTextPreference) findPreference("wifi_ssid");
        mName.setSummary(mSharedPreferences.getString("wifi_ssid", IConstants.Wifi.SSID));
        mName.setEnabled(false);
        //wifi 自动切换
        SwitchPreference auto = (SwitchPreference) findPreference("wifi_auto");
        auto.setChecked(AppConfiguration.WIFI_AUTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    if (preference instanceof EditTextPreference) {
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "")
        );

        /**
         * 设置点击每个文输入框弹出的dialog在点击外部时dialog不被取消
         */
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialog dialog = ((EditTextPreference) preference).getDialog();
                if (dialog != null) {
                    dialog.setCanceledOnTouchOutside(false);
                }
                return false;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        switch (key) {
            case "wifi_ssid":
                EditTextPreference ssid = (EditTextPreference) pref;
                pref.setSummary(ssid.getText());
                AppConfiguration.WIFI_SSID = ssid.getText();
                //重新连接网络
                mWifiUtils.checkAndConnect(AppConfiguration.WIFI_SSID);
                break;
            case "wifi_auto":
                //wifi 是否自动切换
                SwitchPreference auto = (SwitchPreference) pref;
                if (auto.isChecked()) {
                    AppConfiguration.WIFI_AUTO = true;
                    sendBroadcast(new Intent(IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_ENABLE));
                } else {
                    AppConfiguration.WIFI_AUTO = false;
                    sendBroadcast(new Intent(IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_DISABLE));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
