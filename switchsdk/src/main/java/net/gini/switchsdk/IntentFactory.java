package net.gini.switchsdk;


import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_DISCARD;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_KEEP;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_IMAGE_URI;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_TITLE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_FAILED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_EXIT_DIALOG_TEXT;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_NEGATIVE_COLOR;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_POSITIVE_COLOR;
import static net.gini.switchsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_THEME;

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
    private final int mTheme;

    IntentFactory(final TariffSdk tariffSdk) {
        mContext = tariffSdk.getContext();
        mButtonSelectorStyle = tariffSdk.getButtonSelector();
        mButtonTextColor = tariffSdk.getButtonTextColor();
        mPositiveColor = tariffSdk.getPositiveColor();
        mNegativeColor = tariffSdk.getNegativeColor();
        mTheme = tariffSdk.getTheme();
        mExitDialogText = tariffSdk.getExitDialogText();
        mAnalyzedText = tariffSdk.getAnalyzedText();
        mAnalyzedImage = tariffSdk.getAnalyzedImage();
        mAnalyzedTextColor = tariffSdk.getAnalyzedTextColor();
        mAnalyzedTextSize = tariffSdk.getAnalyzedTextSize();
        mExtractionEditTextColor = tariffSdk.getExtractionEditTextColor();
        mExtractionHintColor = tariffSdk.getExtractionHintColor();
        mExtractionLineColor = tariffSdk.getExtractionLineColor();
        mExtractionEditTextBackgroundColor = tariffSdk.getExtractionEditTextBackgroundColor();
        mExtractionButtonText = tariffSdk.getExtractionButtonText();
        mExtractionTitleText = tariffSdk.getExtractionTitleText();
        mReviewTitle = tariffSdk.getReviewTitleText();
        mReviewDiscardText = tariffSdk.getReviewDiscardText();
        mReviewKeepText = tariffSdk.getReviewKeepText();
        mPreviewSuccessText = tariffSdk.getPreviewSuccessText();
        mPreviewFailedText = tariffSdk.getPreviewFailedText();
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

    Intent createTariffSdkIntent() {
        final Intent intent = new Intent(mContext, TakePictureActivity.class);
        addDefaultExtras(intent);
        intent.putExtra(BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT, mPreviewSuccessText);
        intent.putExtra(BUNDLE_EXTRA_PREVIEW_FAILED_TEXT, mPreviewFailedText);

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
