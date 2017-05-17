package net.gini.tariffsdk;


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
    private final int mButtonSelectorStyle;
    private final int mButtonTextColor;

    private final Context mContext;
    private final int mExitDialogText;
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
    }

    Intent createExtractionsActivity() {
        final Intent intent = new Intent(mContext, ExtractionsActivity.class);
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
