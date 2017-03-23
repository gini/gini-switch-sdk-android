package net.gini.tariffsdk;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import net.gini.tariffsdk.takepicture.TariffSdkIntentCreator;

/**
 * <p>
 * This class represents the Gini Tariff SDK. To create an instance of it the {@link TariffSdk.SdkBuilder} should be used.
 * </p>
 */
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
        private int mLoadingView = -1;
        private boolean mShow = false;
        private int mTheme = -1;

        public SdkBuilder(@NonNull final Context context) {
            mContext = context;
        }

        /**
         * <p>
         * Generate a TariffSdk instance from the current builder
         * </p>
         * @return a TariffSdk instance
         */
        public TariffSdk createSdk() {
            return new TariffSdk(mContext.getApplicationContext(), mTheme);
        }

        /**
         * <p>
         * Set a specific loading view which is being shown during the extraction receiving
         * </p>
         * @param loadingView the resource id of the view
         */
        public SdkBuilder setLoadingView(@LayoutRes final int loadingView) {
            mLoadingView = loadingView;
            return this;
        }

        /**
         * <p>
         * Always show the onboarding nevertheless it already has been shown
         * </p>
         * @param show boolean if onboarding should be shown always
         */
        public SdkBuilder alwaysShowOnboarding(final boolean show) {
            mShow = show;
            return this;
        }

        /**
         * <p>
         * Set a specific theme for the SDK Activities, if not set the default app theme is used
         * </p>
         * @param theme the resource id of the theme
         */
        public SdkBuilder setTheme(@StyleRes final int theme) {
            mTheme = theme;
            return this;
        }
    }
}
