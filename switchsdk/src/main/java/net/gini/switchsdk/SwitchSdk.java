package net.gini.switchsdk;


import static android.support.annotation.VisibleForTesting.PACKAGE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;

import net.gini.switchsdk.authentication.AuthenticationService;
import net.gini.switchsdk.authentication.AuthenticationServiceImpl;
import net.gini.switchsdk.authentication.models.ClientCredentials;
import net.gini.switchsdk.authentication.user.UserManager;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.SwitchApi;
import net.gini.switchsdk.utils.Logging;

import okhttp3.OkHttpClient;

/**
 * <p>
 * This class represents the Gini Switch SDK.
 * </p>
 */
public class SwitchSdk {


    public static final int EXTRACTIONS_AVAILABLE = 4;
    public static final int NO_EXTRACTIONS_AVAILABLE = 5;
    public static final int REQUEST_CODE = 666;
    @VisibleForTesting
    @SuppressLint("StaticFieldLeak") //application context is fine
    static volatile SwitchSdk mSingleton;
    private final Context mContext;
    private final DocumentService mDocumentService;
    private final ExtractionService mExtractionService;
    private final RemoteConfigManager mRemoteConfigManager;
    private int mAnalyzedImage;
    private int mAnalyzedText;
    private int mAnalyzedTextColor;
    private int mAnalyzedTextSize;
    private int mButtonSelector;
    private int mButtonTextColor;
    private int mExitDialogText;
    private int mExtractionButtonText;
    private int mExtractionEditTextBackgroundColor;
    private int mExtractionEditTextColor;
    private int mExtractionHintColor;
    private int mExtractionLineColor;
    private int mExtractionTitleText;
    private int mNegativeColor;
    private int mPositiveColor;
    private int mPreviewFailedText;
    private int mPreviewSuccessText;
    private int mReviewDiscardText;
    private int mReviewKeepText;
    private int mReviewTitleText;
    private int mTheme;

    private SwitchSdk(final Context context,
            final DocumentService documentService,
            final ExtractionService extractionService,
            final RemoteConfigManager remoteConfigManager) {

        mContext = context.getApplicationContext();
        mDocumentService = documentService;
        mExtractionService = extractionService;
        mRemoteConfigManager = remoteConfigManager;
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
        mExtractionButtonText = R.string.button_extractions;
        mExtractionTitleText = R.string.extractions_title;
        mReviewTitleText = R.string.review_screen_title;
        mReviewDiscardText = R.string.review_discard_button;
        mReviewKeepText = R.string.review_keep_button;
        mPreviewSuccessText = R.string.preview_analyze_success;
        mPreviewFailedText = R.string.preview_analyze_failed;

        mRemoteConfigManager.requestRemoteConfig();

    }

    /**
     * Returns an instance of the Switch SDK. Be sure that it is initialised before calling this
     * method.
     *
     * @return the SDK instance
     */
    @NonNull
    public static SwitchSdk getSdk() {
        if (mSingleton == null) {
            throw new UnsupportedOperationException(
                    "SDK has not been initialized, call the init method to do so.");
        }
        return mSingleton;
    }

    public static SwitchSdk init(@NonNull final Context context, @NonNull final String clientId,
            @NonNull final String clientPw, @NonNull final String domain,
            @NonNull final OkHttpClient okHttpClient) {
        assertNotNull(context);
        assertNotNull(clientId);
        assertNotNull(clientPw);
        assertNotNull(domain);
        assertNotNull(okHttpClient);
        SwitchApi switchApi = createSwitchApi(context, clientId, clientPw, domain, okHttpClient);
        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(switchApi);

        return create(context, new DocumentServiceImpl(context, switchApi),
                new ExtractionServiceImpl(switchApi),
                remoteConfigManager);
    }

    public static SwitchSdk init(@NonNull final Context context, @NonNull final String clientId,
            @NonNull final String clientPw, @NonNull final String domain) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        return init(context, clientId, clientPw, domain, okHttpClient);
    }

    @VisibleForTesting(otherwise = PACKAGE_PRIVATE)
    public void cleanUp() {
        mDocumentService.cleanup();
        mExtractionService.cleanup();
        mSingleton = null;
    }

    /**
     * <p>
     * Use this to receive the extractions init the SDK.
     * </p>
     *
     * @return the found extractions, or null
     */
    @Nullable
    public Extractions getExtractions() {
        return mExtractionService.getExtractions();
    }

    /**
     * Use this to get an intent of the Switch SDK
     * {@link TakePictureActivity}
     * activity.
     * Only use this method to instantiate an intent of this activity, otherwise an exception will
     * be thrown.
     * To start the activity the method {@link android.app.Activity#startActivityForResult(Intent,
     * int)} with the request code init {@link SwitchSdk#REQUEST_CODE} has to be used.
     *
     * @return an intent of {@link TakePictureActivity}
     */
    @NonNull
    public Intent getSwitchSdkIntent() {

        return new IntentFactory(this).createSwitchSdkIntent();
    }

    /**
     * Call this to provide valuable feedback about the received extractions. The more feedback we
     * get the more precise the extractions will be in the future. To create an extractions feedback
     * object use the provided {@link Extractions#newBuilder(Extractions)} method.
     *
     * @param extractions the reviewed extractions
     */
    public void provideFeedback(@NonNull final Extractions extractions) {
        mExtractionService.sendExtractions(extractions);
        cleanUp();
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
    public SwitchSdk setButtonStyleSelector(@DrawableRes final int selector) {
        mButtonSelector = selector;
        return this;
    }

    /**
     * Set the log level of the SDK. The levels are similar to the ones available for {@link
     * android.util.Log}. You can choose between: ALL, VERBOSE, DEBUG, ERROR, WARN, INFO. {@link
     * net.gini.switchsdk.utils.Logging.LogLevel}
     * Default is that all log levels are shown.
     *
     * @param logLevel the desired log level
     */
    public SwitchSdk setLoggingLevel(final Logging.LogLevel logLevel) {
        Logging.LOG_LEVEL = logLevel;
        return this;
    }

    /**
     * Use this method to turn on logging in the SDK. Logging is turned off by default and should
     * not be turned on for production(or removed via Proguard)
     *
     * @param show true if logging should be shown, false if not
     * @return the instance of the available SDK
     */
    public SwitchSdk showLogging(final boolean show) {
        Logging.SHOW_LOGS = show;
        return this;
    }

    @VisibleForTesting
    static SwitchSdk create(final Context context, DocumentService documentService,
            ExtractionService extractionService, RemoteConfigManager remoteConfigManager) {
        if (mSingleton == null) {
            synchronized (SwitchSdk.class) {
                if (mSingleton == null) {
                    mSingleton = new SwitchSdk(context, documentService, extractionService,
                            remoteConfigManager);
                }
            }
        }
        return mSingleton;
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
    public SwitchSdk setAnalyzedImage(@DrawableRes final int image) {
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
    public SwitchSdk setAnalyzedText(@StringRes final int text) {
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
    public SwitchSdk setAnalyzedTextColor(@ColorRes final int color) {
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
    public SwitchSdk setAnalyzedTextSize(final int size) {
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
    public SwitchSdk setButtonTextColor(@ColorRes final int color) {
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
    public SwitchSdk setExitDialogText(@StringRes final int text) {
        mExitDialogText = text;
        return this;
    }

    int getExtractionButtonText() {
        return mExtractionButtonText;
    }

    /**
     * Set the text for the button on the extraction Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setExtractionButtonText(@StringRes final int text) {
        mExtractionButtonText = text;
        return this;
    }

    int getExtractionEditTextBackgroundColor() {
        return mExtractionEditTextBackgroundColor;
    }

    /**
     * Set the background color of the edit text in the extraction Screen.
     *
     * @param color the resource id of the text color
     * @return the instance of the available SDK
     */
    public SwitchSdk setExtractionEditTextBackgroundColor(@ColorRes final int color) {
        mExtractionEditTextBackgroundColor = color;
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
    public SwitchSdk setExtractionEditTextColor(@ColorRes final int color) {
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
    public SwitchSdk setExtractionHintColor(@ColorRes final int color) {
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
    public SwitchSdk setExtractionLineColor(@ColorRes final int color) {
        mExtractionLineColor = color;
        return this;
    }

    ExtractionService getExtractionService() {
        return mExtractionService;
    }

    int getExtractionTitleText() {
        return mExtractionTitleText;
    }

    /**
     * Set the text for the title on the extraction Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setExtractionTitleText(@StringRes final int text) {
        mExtractionTitleText = text;
        return this;
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
    public SwitchSdk setNegativeColor(@ColorRes final int color) {
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
    public SwitchSdk setPositiveColor(@ColorRes final int color) {
        mPositiveColor = color;
        return this;
    }

    int getPreviewFailedText() {
        return mPreviewFailedText;
    }

    /**
     * Set the title text when the analyzing failed for the Preview Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setPreviewFailedText(@StringRes final int text) {
        mPreviewFailedText = text;
        return this;
    }

    int getPreviewSuccessText() {
        return mPreviewSuccessText;
    }

    /**
     * Set the title text when the analyzing was successful for the Preview Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setPreviewSuccessText(@StringRes final int text) {
        mPreviewSuccessText = text;
        return this;
    }

    int getReviewDiscardText() {
        return mReviewDiscardText;
    }

    /**
     * Set the text for the button to discard the taken image in the Review Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setReviewDiscardText(@StringRes final int text) {
        mReviewDiscardText = text;
        return this;
    }

    int getReviewKeepText() {
        return mReviewKeepText;
    }

    /**
     * Set the text for the button to keep the taken image in the Review Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setReviewKeepText(@StringRes final int text) {
        mReviewKeepText = text;
        return this;
    }

    int getReviewTitleText() {
        return mReviewTitleText;
    }

    /**
     * Set the text for the Review Screen.
     *
     * @param text the resource id of the text
     * @return the instance of the available SDK
     */
    public SwitchSdk setReviewTitleText(@StringRes final int text) {
        mReviewTitleText = text;
        return this;
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
    public SwitchSdk setTheme(@StyleRes final int theme) {
        mTheme = theme;
        return this;
    }

    private static <T> T assertNotNull(T parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        return parameter;
    }

    @NonNull
    private static SwitchApi createSwitchApi(final @NonNull Context context,
            final @NonNull String clientId, final @NonNull String clientPw,
            final @NonNull String domain, final OkHttpClient okHttpClient) {
        ClientCredentials clientCredentials = new ClientCredentials(clientId, clientPw);
        UserApiImpl userApi = new UserApiImpl(clientCredentials, okHttpClient);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(
                userApi, new UserManager(context, domain));
        final SwitchApiImpl switchApi = new SwitchApiImpl(okHttpClient, authenticationService);
        authenticationService.init(new NetworkCallback<Void>() {
            @Override
            public void onError(final Exception e) {
                //TODO error what are we going to do here?
            }

            @Override
            public void onSuccess(final Void aVoid) {
                //everything is fine
            }
        });

        return switchApi;
    }
}
