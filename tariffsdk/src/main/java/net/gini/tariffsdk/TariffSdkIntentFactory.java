package net.gini.tariffsdk;


import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_THEME;

import android.content.Context;
import android.content.Intent;

final class TariffSdkIntentFactory {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";

    private final Context mContext;

    private final int mTheme;

    TariffSdkIntentFactory(final Context context, final int theme) {
        mContext = context;
        mTheme = theme;
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
