package net.gini.tariffsdk.takepicture;


import android.content.Context;
import android.content.Intent;

final public class TariffSdkIntentCreator {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";
    static final String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    private final Context mContext;

    private final int mTheme;

    public TariffSdkIntentCreator(final Context context, final int theme) {
        mContext = context;
        mTheme = theme;
    }

    public Intent createIntent() {
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.putExtra(BUNDLE_EXTRA_RIGHT_INSTANTIATED, true);
        intent.putExtra(BUNDLE_EXTRA_THEME, mTheme);
        return intent;
    }

}
