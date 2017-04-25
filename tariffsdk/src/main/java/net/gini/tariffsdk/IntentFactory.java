package net.gini.tariffsdk;


import static net.gini.tariffsdk.ReviewPictureActivity.BUNDLE_EXTRA_IMAGE_URI;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_THEME;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

final class IntentFactory {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";

    private final Context mContext;

    private final int mTheme;

    IntentFactory(final TariffSdk tariffSdk) {
        mContext = tariffSdk.getContext();
        mTheme = tariffSdk.getTheme();
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

    Intent createTakePictureActivity() {
        final Intent intent = new Intent(mContext, TakePictureActivity.class);
        addDefaultExtras(intent);
        return intent;
    }

    Intent createTariffSdkIntent() {
        final Intent intent = new Intent(mContext, TariffSdkActivity.class);
        addDefaultExtras(intent);
        return intent;
    }

    private void addDefaultExtras(final Intent intent) {
        intent.putExtra(BUNDLE_EXTRA_RIGHT_INSTANTIATED, true);
        intent.putExtra(BUNDLE_EXTRA_THEME, mTheme);
    }

}
