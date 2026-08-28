package com.android.settings.display;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import com.android.settings.core.TogglePreferenceController;

public class SecondDisplayEnableController extends TogglePreferenceController {

    private static final String SYS_KEY = "sec_display_mode";
    private static final String PROP_KEY = "persist.vendor.secdisplaymode";

    public SecondDisplayEnableController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean isChecked() {
        String currentProp = SystemProperties.get(PROP_KEY, "2");
        int mode;
        try {
            mode = Integer.parseInt(currentProp);
        } catch (NumberFormatException e) {
            mode = Settings.Global.getInt(mContext.getContentResolver(), SYS_KEY, 2);
        }
        return mode != 0;
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        int val;
        if (isChecked) {
            val = Settings.Global.getInt(mContext.getContentResolver(), SYS_KEY, 2);
            if (val == 0) val = 2;
        } else {
            val = 0;
        }

        String valStr = String.valueOf(val);

        Settings.Global.putInt(mContext.getContentResolver(), SYS_KEY, val);

        try {
            SystemProperties.set(PROP_KEY, valStr);
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return 0;
    }
}
