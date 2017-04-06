package net.gini.tariffsdk.configuration;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

class RemoteConfigStore {

    private static final String REMOTE_CONFIG_KEY_FLASH = "REMOTE_CONFIG_KEY_FLASH";
    private static final String REMOTE_CONFIG_KEY_RESOLUTION = "REMOTE_CONFIG_KEY_RESOLUTION";
    @VisibleForTesting
    static final String REMOTE_CONFIG_STORE_SHARED_PREFS =
            "REMOTE_CONFIG_STORE_SHARED_PREFS";

    private final Context mContext;

    public RemoteConfigStore(@NonNull final Context context) {
        mContext = context;
    }

    public long getMaximalCameraResolution() {
        return getSharedPreferences().getLong(REMOTE_CONFIG_KEY_RESOLUTION, -1);
    }

    public void storeMaximalCameraResolution(final long maxCameraResolution) {
        getSharedPreferences().edit().putLong(REMOTE_CONFIG_KEY_RESOLUTION, maxCameraResolution)
                .apply();
    }

    public void storeUseFlash(final int flashMode) {
        getSharedPreferences().edit().putInt(REMOTE_CONFIG_KEY_FLASH, flashMode).apply();
    }

    public int getFlashMode() {
        //TODO specify default value
        return getSharedPreferences().getInt(REMOTE_CONFIG_KEY_FLASH, 0);
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(REMOTE_CONFIG_STORE_SHARED_PREFS, MODE_PRIVATE);
    }
}
