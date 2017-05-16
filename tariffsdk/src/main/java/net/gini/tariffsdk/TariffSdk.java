package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * <p>
 * This class represents the Gini Tariff SDK.
 * </p>
 */
public class TariffSdk {


    public static final int EXTRACTIONS_AVAILABLE = 4;
    public static final int REQUEST_CODE = 666;
    @SuppressLint("StaticFieldLeak") //application context is fine
    private static volatile TariffSdk mSingleton;
    private final Context mContext;
    private final DocumentService mDocumentService;
    private final ExtractionService mExtractionService;
    private int mButtonSelector;
    private int mButtonTextColor;
    private int mNegativeColor;
    private OkHttpClient mOkHttpClient;
    private int mPositiveColor;

    private TariffSdk(final Context context, final String clientId, final String clientPw,
            final DocumentService authenticationService,
            final ExtractionService extractionService) {

        mContext = context.getApplicationContext();
        mDocumentService = authenticationService;
        mExtractionService = extractionService;
        mPositiveColor = R.color.positiveColor;
        mNegativeColor = R.color.negativeColor;
    }

    public static TariffSdk init(@NonNull final Context context, @NonNull final String clientId,
            @NonNull final String clientPw, @NonNull final String domain) {
        assertNotNull(context);
        assertNotNull(clientId);
        assertNotNull(clientPw);
        assertNotNull(domain);
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
    public Set<Extraction> getExtractions() {
        //TODO
        cleanUp();
        return mExtractionService.getExtractions();
    }

    /**
     * <p>
     * Use this to get an intent of the Tariff SDK
     * {@link TakePictureActivity}
     * activity.
     * Only use this method to instantiate an intent of this activity, otherwise an exception will
     * be thrown.
     * To start the activity the method {@link android.app.Activity#startActivityForResult(Intent,
     * int)} with the request code init {@link TariffSdk#REQUEST_CODE} has to be used.
     * </p>
     *
     * @return an intent of {@link TakePictureActivity}
     */
    @NonNull
    public Intent getTariffSdkIntent() {

        return new IntentFactory(this).createTariffSdkIntent();
    }

    /**
     * Use this to set a custom button style. The style has to be a selector and should provide
     * different states the button can have, e.g. pressed etc. See {@link
     * <a href="https://developer.android.com/guide/topics/resources/drawable-resource.html#StateList">Official StateList documentation</a>} for more information.
     * @param selector as a drawable resource.
     * @return the instance of the available SDK
     */
    public TariffSdk setButtonStyleSelector(@DrawableRes final int selector) {
        mButtonSelector = selector;
        return this;
    }

    /**
     * <p>
     * Use this if an OkHttpClient has been already created, since OkHttpClient should be a
     * singleton.
     * </p>
     *
     * @param okHttpClient the created okHttpClient
     * @return the instance of the available SDK
     */
    public TariffSdk withOkHttpClient(@NonNull OkHttpClient okHttpClient) {
        mOkHttpClient = assertNotNull(okHttpClient);
        return this;
    }

    void cleanUp() {
        mDocumentService.cleanup();
    }

    int getButtonSelector() {
        return mButtonSelector;
    }

    int getButtonTextColor() {
        return mButtonTextColor;
    }

    /**
     * Use this to set a custom button text color.
     * @param color as a color resource id.
     * @return the instance of the available SDK
     */
    public TariffSdk setButtonTextColor(@ColorRes final int color) {
        mButtonTextColor = color;
        return this;
    }

    Context getContext() {
        return mContext;
    }

    DocumentService getDocumentService() {
        return mDocumentService;
    }

    ExtractionService getExtractionService() {
        return mExtractionService;
    }

    int getNegativeColor() {
        return mNegativeColor;
    }

    /**
     * Use this to set the negative color which indicates that a process did fail or something went
     * wrong. The color should indicate failure and can therefore be something like red.
     *
     * @param color as a color resource int.
     * @return the instance of the available SDK
     */
    public TariffSdk setNegativeColor(@ColorRes final int color) {
        mNegativeColor = color;
        return this;
    }

    int getPositiveColor() {
        return mPositiveColor;
    }

    /**
     * Use this to set the positive color which indicates that a process did succeed. The color
     * should indicate success and can therefore be something like green.
     *
     * @param color as a color resource int.
     * @return the instance of the available SDK
     */
    public TariffSdk setPositiveColor(@ColorRes final int color) {
        mPositiveColor = color;
        return this;
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
