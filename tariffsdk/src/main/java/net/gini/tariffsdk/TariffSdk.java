package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;

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
    @VisibleForTesting
    @SuppressLint("StaticFieldLeak") //application context is fine
    static volatile TariffSdk mSingleton;
    private final Context mContext;
    private final DocumentService mDocumentService;
    private final ExtractionService mExtractionService;
    private int mAnalyzedImage;
    private int mAnalyzedText;
    private int mAnalyzedTextColor;
    private int mAnalyzedTextSize;
    private int mButtonSelector;
    private int mButtonTextColor;
    private int mExitDialogText;
    private int mExtractionEditTextBackgroundColor;
    private int mExtractionEditTextColor;
    private int mExtractionHintColor;
    private int mExtractionLineColor;
    private int mNegativeColor;
    private OkHttpClient mOkHttpClient;
    private int mPositiveColor;
    private int mTheme;

    private TariffSdk(final Context context, final String clientId, final String clientPw,
            final DocumentService authenticationService,
            final ExtractionService extractionService) {

        mContext = context.getApplicationContext();
        mDocumentService = authenticationService;
        mExtractionService = extractionService;
        mPositiveColor = R.color.positiveColor;
        mNegativeColor = R.color.negativeColor;
        mTheme = R.style.GiniTheme;
        mExitDialogText = R.string.exit_dialog_text;
        mAnalyzedText = R.string.analyzed_text;
        mAnalyzedImage = R.drawable.ic_check_circle;
        mAnalyzedTextColor = R.color.primaryText;
        mAnalyzedTextSize = context.getResources().getInteger(R.integer.analyzed_text_size);
        mExtractionEditTextColor = R.color.primaryText;
        mExtractionHintColor = R.color.secondaryText;
        mExtractionLineColor = R.color.primaryText;
        mExtractionEditTextBackgroundColor = R.color.secondaryColor;
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

    public int getExtractionEditTextBackgroundColor() {
        return mExtractionEditTextBackgroundColor;
    }

    /**
     * Set the background color of the edit text in the extraction Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public TariffSdk setExtractionEditTextBackgroundColor(@ColorRes final int color) {
        mExtractionEditTextBackgroundColor = color;
        return this;
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
     * Use this to get an intent of the Tariff SDK
     * {@link TakePictureActivity}
     * activity.
     * Only use this method to instantiate an intent of this activity, otherwise an exception will
     * be thrown.
     * To start the activity the method {@link android.app.Activity#startActivityForResult(Intent,
     * int)} with the request code init {@link TariffSdk#REQUEST_CODE} has to be used.
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
     * <a href="https://developer.android.com/guide/topics/resources/drawable-resource.html#StateList">Official
     * StateList documentation</a>} for more information.
     *
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

    int getAnalyzedImage() {
        return mAnalyzedImage;
    }

    /**
     * Set the image for the Analysing Completed Screen.
     *
     * @param image the resource id of the image
     * @return the instance of the available SDK
     */
    public TariffSdk setAnalyzedImage(@DrawableRes final int image) {
        mAnalyzedImage = image;
        return this;
    }

    int getAnalyzedText() {
        return mAnalyzedText;
    }

    /**
     * Set the text for the Analysing Completed Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public TariffSdk setAnalyzedText(@StringRes final int text) {
        mAnalyzedText = text;
        return this;
    }

    int getAnalyzedTextColor() {
        return mAnalyzedTextColor;
    }

    /**
     * Set the text color for the Analysing Completed Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public TariffSdk setAnalyzedTextColor(@ColorRes final int color) {
        mAnalyzedTextColor = color;
        return this;
    }

    int getAnalyzedTextSize() {
        return mAnalyzedTextSize;
    }

    /**
     * Set the text size for the Analysing Completed Screen. Note that it will set the size in sp.
     * See {@link
     * <a href="https://developer.android.com/guide/topics/resources/more-resources.html#Dimension">Dimension
     * documentation</a>} for more information.
     *
     * @param size the size of the analyzed text
     * @return the instance of the available SDK
     */
    public TariffSdk setAnalyzedTextSize(final int size) {
        mAnalyzedTextSize = size;
        return this;
    }

    int getButtonSelector() {
        return mButtonSelector;
    }

    int getButtonTextColor() {
        return mButtonTextColor;
    }

    /**
     * Use this to set a custom button text color.
     *
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

    int getExitDialogText() {
        return mExitDialogText;
    }

    /**
     * Use this to set a custom text for the cancel dialog.
     *
     * @param text as a string resource id.
     * @return the instance of the available SDK
     */
    public TariffSdk setExitDialogText(@StringRes final int text) {
        mExitDialogText = text;
        return this;
    }

    int getExtractionEditTextColor() {
        return mExtractionEditTextColor;
    }

    /**
     * Set the edit text color for the extraction Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public TariffSdk setExtractionEditTextColor(@ColorRes final int color) {
        mExtractionEditTextColor = color;
        return this;
    }

    int getExtractionHintColor() {
        return mExtractionHintColor;
    }

    /**
     * Set the hint color of the edit text in the extraction Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public TariffSdk setExtractionHintColor(@ColorRes final int color) {
        mExtractionHintColor = color;
        return this;
    }

    int getExtractionLineColor() {
        return mExtractionLineColor;
    }

    /**
     * Set the line color of the edit text in the extraction Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public TariffSdk setExtractionLineColor(@ColorRes final int color) {
        mExtractionLineColor = color;
        return this;
    }

    ExtractionService getExtractionService() {
        return mExtractionService;
    }

    int getNegativeColor() {
        return mNegativeColor;
    }

    /**
     * Use this to set the negative color which indicates that a process failed or something went
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
     * Use this to set the positive color which indicates that a process succeeded. The color
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

    int getTheme() {
        return mTheme;
    }

    /**
     * Set a specific theme for the SDK Activities, if not set the default app theme is used.
     * Use this to set the accent color which will be used for dialogs, loading indicators and
     * others.
     *
     * @param theme the resource id of the theme
     * @return the instance of the available SDK
     */
    public TariffSdk setTheme(@StyleRes final int theme) {
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
