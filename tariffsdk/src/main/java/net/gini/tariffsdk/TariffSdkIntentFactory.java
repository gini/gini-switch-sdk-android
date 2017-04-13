package net.gini.tariffsdk;


import android.content.Context;
import android.content.Intent;

import net.gini.tariffsdk.takepictures.TakePictureActivity;

final class TariffSdkIntentFactory {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";
    static final String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    private final Context mContext;

    private final int mTheme;

    TariffSdkIntentFactory(final Context context, final int theme) {
        mContext = context;
        mTheme = theme;
    }

    Intent createTakePictureIntent() {
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
