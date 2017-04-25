package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * <p>
 * This class represents the Gini Tariff SDK.
 * </p>
 */
public class TariffSdk {


    public static final int EXTRACTIONS_AVAILABLE = 1;
    public static final int REQUEST_CODE = 666;
    @SuppressLint("StaticFieldLeak") //application context is fine
    private static volatile TariffSdk mSingleton;
    private final Context mContext;
    private final DocumentService mDocumentService;
    private final ExtractionService mExtractionService;
    private int mLoadingView;
    private OkHttpClient mOkHttpClient;
    private int mTheme;

    private TariffSdk(final Context context, final String clientId, final String clientPw,
            final DocumentService authenticationService,
            final ExtractionService extractionService) {

        mContext = context.getApplicationContext();
        mDocumentService = authenticationService;
        mExtractionService = extractionService;
    }

    public static TariffSdk init(final Context context, @NonNull final String clientId,
            @NonNull final String clientPw) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        if (mSingleton == null) {
            synchronized (TariffSdk.class) {
                if (mSingleton == null) {
                    mSingleton = new TariffSdk(context, clientId, clientPw,
                            new DocumentServiceImpl(context),
                            new ExtractionServiceImpl());
                }
            }
        }
        return mSingleton;
    }

    /**
     * <p>
     * Use this to receive the extractions init the SDK.
     * </p>
     *
     * @return the found extractions inside a list
     */
    public List<Extractions> getExtractions() {
        //TODO
        return mExtractionService.getExtractions();
    }

    /**
     * <p>
     * Use this to get an intent of the Tariff SDK
     * {@link TariffSdkActivity}
     * activity.
     * Only use this method to instantiate an intent of this activity, otherwise an exception will
     * be thrown.
     * To start the activity the method {@link android.app.Activity#startActivityForResult(Intent,
     * int)} with the request code init {@link TariffSdk#REQUEST_CODE} has to be used.
     * </p>
     *
     * @return an intent of {@link TariffSdkActivity}
     */
    @NonNull
    public Intent getTariffSdkIntent() {

        return new TariffSdkIntentFactory(mContext, mTheme).createTariffSdkIntent();
    }

    /**
     * <p>
     * Set a specific loading view which is being shown during the extraction receiving
     * </p>
     *
     * @param loadingView the resource id of the view
     */
    public TariffSdk withLoadingView(@LayoutRes final int loadingView) {
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
    public TariffSdk withOkHttpClient(@NonNull OkHttpClient okHttpClient) {
        mOkHttpClient = assertNotNull(okHttpClient);
        return this;
    }

    /**
     * <p>
     * Set a specific theme for the SDK Activities, if not set the default app theme is used
     * </p>
     *
     * @param theme the resource id of the theme
     */
    public TariffSdk withTheme(@StyleRes final int theme) {
        mTheme = theme;
        return this;
    }

    DocumentService getDocumentService() {
        return mDocumentService;
    }

    static TariffSdk getSdk() {
        return mSingleton;
    }

    private static <T> T assertNotNull(T parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        return parameter;
    }
}
