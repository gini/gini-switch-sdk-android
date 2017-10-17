package net.gini.switchsdk;


import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_DISCARD;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_KEEP;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_IMAGE_URI;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_TITLE;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_EXIT_DIALOG_TEXT;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_NEGATIVE_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_POSITIVE_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_THEME;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_FAILED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_SKIP_ANALYZING_COMPLETED_SCREEN;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

final class IntentFactory {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";
    private final int mAnalyzedImage;
    private final int mAnalyzedText;
    private final int mAnalyzedTextColor;
    private final int mAnalyzedTextSize;
    private final int mButtonSelectorStyle;
    private final int mButtonTextColor;
    private final Context mContext;
    private final int mExitDialogText;
    private final int mExtractionButtonText;
    private final int mExtractionEditTextBackgroundColor;
    private final int mExtractionEditTextColor;
    private final int mExtractionHintColor;
    private final int mExtractionLineColor;
    private final int mExtractionTitleText;
    private final int mNegativeColor;
    private final int mPositiveColor;
    private final int mPreviewFailedText;
    private final int mPreviewSuccessText;
    private final int mReviewDiscardText;
    private final int mReviewKeepText;
    private final int mReviewTitle;
    private final boolean mSkipAnalyzingCompleteScreen;
    private final int mTheme;

    IntentFactory(final SwitchSdk switchSdk) {
        mContext = switchSdk.getContext();
        mButtonSelectorStyle = switchSdk.getButtonSelector();
        mButtonTextColor = switchSdk.getButtonTextColor();
        mPositiveColor = switchSdk.getPositiveColor();
        mNegativeColor = switchSdk.getNegativeColor();
        mTheme = switchSdk.getTheme();
        mExitDialogText = switchSdk.getExitDialogText();
        mAnalyzedText = switchSdk.getAnalyzedText();
        mAnalyzedImage = switchSdk.getAnalyzedImage();
        mAnalyzedTextColor = switchSdk.getAnalyzedTextColor();
        mAnalyzedTextSize = switchSdk.getAnalyzedTextSize();
        mExtractionEditTextColor = switchSdk.getExtractionEditTextColor();
        mExtractionHintColor = switchSdk.getExtractionHintColor();
        mExtractionLineColor = switchSdk.getExtractionLineColor();
        mExtractionEditTextBackgroundColor = switchSdk.getExtractionEditTextBackgroundColor();
        mExtractionButtonText = switchSdk.getExtractionButtonText();
        mExtractionTitleText = switchSdk.getExtractionTitleText();
        mReviewTitle = switchSdk.getReviewTitleText();
        mReviewDiscardText = switchSdk.getReviewDiscardText();
        mReviewKeepText = switchSdk.getReviewKeepText();
        mPreviewSuccessText = switchSdk.getPreviewSuccessText();
        mPreviewFailedText = switchSdk.getPreviewFailedText();
        mSkipAnalyzingCompleteScreen = switchSdk.shouldSkipAnalyzingCompletedScreen();
    }

    Intent createReviewActivity(final Uri uri) {
        final Intent intent = new Intent(mContext, ReviewPictureActivity.class);
        addDefaultExtras(intent);
        intent.putExtra(BUNDLE_EXTRA_TITLE, mReviewTitle);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_DISCARD, mReviewDiscardText);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_KEEP, mReviewKeepText);
        intent.putExtra(BUNDLE_EXTRA_IMAGE_URI, uri);
        return intent;
    }

    Intent createSwitchSdkIntent() {
        final Intent intent = new Intent(mContext, TakePictureActivity.class);
        addDefaultExtras(intent);
        intent.putExtra(BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT, mPreviewSuccessText);
        intent.putExtra(BUNDLE_EXTRA_PREVIEW_FAILED_TEXT, mPreviewFailedText);

        intent.putExtra(BUNDLE_EXTRA_SKIP_ANALYZING_COMPLETED_SCREEN, mSkipAnalyzingCompleteScreen);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, mAnalyzedText);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, mAnalyzedImage);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, mAnalyzedTextColor);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, mAnalyzedTextSize);

        return intent;
    }

    private void addDefaultExtras(final Intent intent) {
        intent.putExtra(BUNDLE_EXTRA_RIGHT_INSTANTIATED, true);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, mButtonSelectorStyle);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, mButtonTextColor);
        intent.putExtra(BUNDLE_EXTRA_POSITIVE_COLOR, mPositiveColor);
        intent.putExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, mNegativeColor);
        intent.putExtra(BUNDLE_EXTRA_THEME, mTheme);
        intent.putExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, mExitDialogText);
    }

}
