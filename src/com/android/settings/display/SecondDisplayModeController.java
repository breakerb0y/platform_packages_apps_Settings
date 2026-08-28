package com.android.settings.display;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;

public class SecondDisplayModeController extends BasePreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String SYS_KEY = "sec_display_mode";
    private static final String PROP_KEY = "persist.vendor.secdisplaymode";

    public SecondDisplayModeController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            String currentProp = SystemProperties.get(PROP_KEY, "2");
            int mode;
            try {
                mode = Integer.parseInt(currentProp);
            } catch (NumberFormatException e) {
                mode = Settings.Global.getInt(mContext.getContentResolver(), SYS_KEY, 2);
            }

            if (mode == 0) {
                mode = 2;
            }

            listPreference.setValue(String.valueOf(mode));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String valStr = (String) newValue;
        int val = Integer.parseInt(valStr);

        Settings.Global.putInt(mContext.getContentResolver(), SYS_KEY, val);

        try {
            SystemProperties.set(PROP_KEY, valStr);
        } catch (Exception e) {
        }

        return true;
    }
}
