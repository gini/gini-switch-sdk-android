package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import net.gini.tariffsdk.takepicture.TariffSdkIntentCreator;

public class TariffSdk {

    public static int REQUEST_CODE = 666;

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static volatile TariffSdk mInstance = null;

    private final Context mContext;

    private final int mTheme;

    private TariffSdk(final Context context, final int theme) {
        mContext = context;
        mTheme = theme;
    }

    @NonNull
    public Intent getTariffSdkIntent() {

        return new TariffSdkIntentCreator(mContext, mTheme).createIntent();
    }

    private static TariffSdk getSdkInstance(@NonNull final Context context, final int theme) {
        if (mInstance == null) {
            synchronized (TariffSdk.class) {
                if (mInstance == null) {
                    mInstance = new TariffSdk(context.getApplicationContext(), theme);
                }
            }
        }
        return mInstance;
    }

    public static class SdkBuilder {
        @NonNull
        private final Context mContext;

        private int mTheme = -1;

        public SdkBuilder(@NonNull final Context context) {
            mContext = context;
        }

        public TariffSdk createSdk() {
            return getSdkInstance(mContext, mTheme);
        }

        /**
         * Set a specific theme for the SDK Activities, if not set the default app theme is used
         *
         * @param theme the resource id of the theme
         */
        public SdkBuilder setTheme(@StyleRes final int theme) {
            mTheme = theme;
            return this;
        }
    }
}
