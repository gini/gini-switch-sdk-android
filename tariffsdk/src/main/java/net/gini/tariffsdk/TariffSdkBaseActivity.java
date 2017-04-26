package net.gini.tariffsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TariffSdkBaseActivity extends AppCompatActivity {

    private static final int NOT_SET = -1;
    protected static String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySettings();
    }

    protected int getThemeResourceId() {
        return getIntent().getIntExtra(TariffSdkBaseActivity.BUNDLE_EXTRA_THEME, NOT_SET);
    }

    private void applySettings() {
        final int theme = getThemeResourceId();
        if (theme != NOT_SET) {
            setTheme(theme);
        }
    }

}
