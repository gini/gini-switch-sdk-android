package net.gini.tariffsdk;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import net.gini.tariffsdk.takepicture.TariffSdkIntentCreator;

public class TariffSdk {

    public static int REQUEST_CODE = 666;

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

    public static class SdkBuilder {

        @NonNull
        private final Context mContext;

        private int mTheme = -1;

        public SdkBuilder(@NonNull final Context context) {
            mContext = context;
        }

        public TariffSdk createSdk() {
            return new TariffSdk(mContext.getApplicationContext(), mTheme);
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
