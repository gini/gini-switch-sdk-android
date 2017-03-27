package net.gini.tariffsdk;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.takepicture.TariffSdkIntentCreator;

import okhttp3.OkHttpClient;

/**
 * <p>
 * This class represents the Gini Tariff SDK. To create an instance of it the {@link
 * TariffSdk.SdkBuilder} should be used.
 * </p>
 */
public class TariffSdk {

    public static int REQUEST_CODE = 666;
    private final AuthenticationService mAuthenticationService;

    private final Context mContext;
    private final OkHttpClient mOkHttpClient;
    private final int mTheme;

    private TariffSdk(final Context context, final int theme, final String clientId,
            final String clientPw, final OkHttpClient okHttpClient) {
        mContext = context;
        mTheme = theme;
        mOkHttpClient = okHttpClient;
        mAuthenticationService = AuthenticationService.getInstance(context, clientId, clientPw,
                okHttpClient);
    }

    /**
     * <p>
     * Use this to get an intent of the Tariff SDK
     * {@link net.gini.tariffsdk.takepicture.CameraActivity}
     * activity.
     * Only use this method to instantiate an intent of this activity, otherwise an exception will
     * be thrown.
     * To start the activity the method {@link android.app.Activity#startActivityForResult(Intent,
     * int)} with the request code from {@link TariffSdk#REQUEST_CODE} has to be used.
     * </p>
     *
     * @return an intent of {@link net.gini.tariffsdk.takepicture.CameraActivity}
     */
    @NonNull
    public Intent getTariffSdkIntent() {

        return new TariffSdkIntentCreator(mContext, mTheme).createIntent();
    }

    /**
     * <p>
     * The Tariff SDK Builder class, use this builder to create an instance of the {@link
     * TariffSdk}.
     * </p>
     */
    public static class SdkBuilder {

        @NonNull
        private final String mClientId;
        @NonNull
        private final String mClientPw;
        @NonNull
        private final Context mContext;
        private int mLoadingView = -1;
        private OkHttpClient mOkHttpClient;
        private boolean mShow = false;
        private int mTheme = -1;

        public SdkBuilder(@NonNull final Context context, @NonNull final String clientId,
                @NonNull final String clientPw) {
            mContext = assertNotNull(context);
            mClientId = assertNotNull(clientId);
            mClientPw = assertNotNull(clientPw);
        }

        /**
         * <p>
         * Always show the onboarding nevertheless it already has been shown
         * </p>
         *
         * @param show boolean if onboarding should be shown always
         * @return the instance of the current builder
         */
        public SdkBuilder alwaysShowOnboarding(final boolean show) {
            mShow = show;
            return this;
        }

        /**
         * <p>
         * Generate a TariffSdk instance from the current builder
         * </p>
         *
         * @return a TariffSdk instance
         */
        public TariffSdk createSdk() {
            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient();
            }
            return new TariffSdk(mContext.getApplicationContext(), mTheme, mClientId, mClientPw,
                    mOkHttpClient);
        }

        /**
         * <p>
         * Set a specific loading view which is being shown during the extraction receiving
         * </p>
         *
         * @param loadingView the resource id of the view
         * @return the instance of the current builder
         */
        public SdkBuilder setLoadingView(@LayoutRes final int loadingView) {
            mLoadingView = loadingView;
            return this;
        }

        /**
         * <p>
         * Use this if an OkHttpClient has been already created, since OkHttpClient should be a
         * singleton.
         * </p>
         *
         * @param okHttpClient the created okHttpClient
         * @return the instance of the current builder
         */
        public SdkBuilder setOkHttpClient(@NonNull OkHttpClient okHttpClient) {
            mOkHttpClient = assertNotNull(okHttpClient);
            return this;
        }

        /**
         * <p>
         * Set a specific theme for the SDK Activities, if not set the default app theme is used
         * </p>
         *
         * @param theme the resource id of the theme
         * @return the instance of the current builder
         */
        public SdkBuilder setTheme(@StyleRes final int theme) {
            mTheme = theme;
            return this;
        }

        private static <T> T assertNotNull(T parameter) {
            if (parameter == null) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
            return parameter;
        }
    }
}
