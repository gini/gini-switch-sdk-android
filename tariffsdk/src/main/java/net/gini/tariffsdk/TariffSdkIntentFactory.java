package net.gini.tariffsdk;


import android.content.Context;
import android.content.Intent;

final class TariffSdkIntentFactory {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";
    static final String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    private final Context mContext;

    private final int mTheme;

    TariffSdkIntentFactory(final Context context, final int theme) {
        mContext = context;
        mTheme = theme;
    }

    Intent createIntent() {
        final Intent intent = new Intent(mContext, TariffSdkActivity.class);
        intent.putExtra(BUNDLE_EXTRA_RIGHT_INSTANTIATED, true);
        intent.putExtra(BUNDLE_EXTRA_THEME, mTheme);
        return intent;
    }

}
