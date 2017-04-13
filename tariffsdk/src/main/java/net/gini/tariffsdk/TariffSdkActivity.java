package net.gini.tariffsdk;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

final public class TariffSdkActivity extends AppCompatActivity {

    private static final int NOT_SET = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCorrectUsage();
        applySettings();

        setContentView(R.layout.activity_tariff_sdk);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void applySettings() {
        final int theme = getIntent().getIntExtra(TariffSdkIntentFactory.BUNDLE_EXTRA_THEME,
                NOT_SET);
        if (theme != NOT_SET) {
            setTheme(theme);
        }
    }

    private void checkForCorrectUsage() {
        if (getCallingActivity() == null) {
            //TODO
            throw new RuntimeException("WRONG!");
        }
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                TariffSdkIntentFactory.BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            //TODO
            throw new RuntimeException("WRONG!");
        }
    }
}
