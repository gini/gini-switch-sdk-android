package net.gini.tariffsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

class TariffSdkBaseActivity extends AppCompatActivity {

    private static final int NOT_SET = 0;
    protected static String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySettings();
        checkForCorrectUsage();
    }

    protected int getThemeResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_THEME, NOT_SET);
    }

    private void applySettings() {
        final int theme = getThemeResourceIdFromBundle();
        if (theme != NOT_SET) {
            setTheme(theme);
        } else {
            setTheme(R.style.GiniTheme);
        }
    }

    private void checkForCorrectUsage() {
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                IntentFactory.BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            throw new IllegalArgumentException(
                    "Do not create this Intent by yourself, use the provided TariffSdk"
                            + ".getTariffSdkIntent() method for it!");
        }
    }

}
