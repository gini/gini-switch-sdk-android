package net.gini.tariffsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TariffSdkBaseActivity extends AppCompatActivity {

    private static final int NOT_SET = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCorrectUsage();
        applySettings();
    }

    protected int getThemeResourceId() {
        return getIntent().getIntExtra(TariffSdkIntentFactory.BUNDLE_EXTRA_THEME, NOT_SET);
    }

    private void applySettings() {
        final int theme = getThemeResourceId();
        if (theme != NOT_SET) {
            setTheme(theme);
        }
    }

    private void checkForCorrectUsage() {
        if (getCallingActivity() == null) {
            throw new IllegalStateException("Start this Intent with startActivityForResult()!");
        }
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                TariffSdkIntentFactory.BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            throw new IllegalArgumentException(
                    "Do not create this Intent by yourself, use the provided TariffSdk"
                            + ".getTariffSdkIntent() method for it!");
        }
    }
}
