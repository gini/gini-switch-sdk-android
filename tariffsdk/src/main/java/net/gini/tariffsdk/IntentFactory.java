package net.gini.tariffsdk;


import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_EDIT_TEXT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_HINT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_LINE_COLOR;
import static net.gini.tariffsdk.ReviewPictureActivity.BUNDLE_EXTRA_IMAGE_URI;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_EXIT_DIALOG_TEXT;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_NEGATIVE_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_POSITIVE_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_THEME;

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
    private final int mExtractionEditTextBackgroundColor;
    private final int mExtractionEditTextColor;
    private final int mExtractionHintColor;
    private final int mExtractionLineColor;
    private final int mNegativeColor;
    private final int mPositiveColor;
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
    }

    Intent createExtractionsActivity() {
        final Intent intent = new Intent(mContext, ExtractionsActivity.class);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, mAnalyzedText);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, mAnalyzedImage);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, mAnalyzedTextColor);
        intent.putExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, mAnalyzedTextSize);
        intent.putExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, mExtractionEditTextColor);
        intent.putExtra(BUNDLE_EXTRA_HINT_COLOR, mExtractionHintColor);
        intent.putExtra(BUNDLE_EXTRA_LINE_COLOR, mExtractionLineColor);
        intent.putExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR,
                mExtractionEditTextBackgroundColor);

        addDefaultExtras(intent);
        return intent;
    }

    Intent createReviewActivity(final Uri uri) {
        final Intent intent = new Intent(mContext, ReviewPictureActivity.class);
        addDefaultExtras(intent);
        intent.putExtra(BUNDLE_EXTRA_IMAGE_URI, uri);
        return intent;
    }

    Intent createTariffSdkIntent() {
        final Intent intent = new Intent(mContext, TakePictureActivity.class);
        addDefaultExtras(intent);
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
