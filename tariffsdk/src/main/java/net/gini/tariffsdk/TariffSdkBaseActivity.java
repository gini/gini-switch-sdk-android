package net.gini.tariffsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

class TariffSdkBaseActivity extends AppCompatActivity {

    private static final int NOT_SET = 0;
    protected static String BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE =
            "BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE";
    protected static String BUNDLE_EXTRA_BUTTON_TEXT_COLOR = "BUNDLE_EXTRA_BUTTON_TEXT_COLOR";
    protected static String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySettings();
        checkForCorrectUsage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("TODO: DUMMY");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                return false;
            }
        });
        return true;
    }

    protected int getButtonStyleResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, NOT_SET);
    }

    protected int getButtonTextColorResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, NOT_SET);
    }

    protected int getThemeResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_THEME, NOT_SET);
    }

    protected boolean hasCustomButtonStyleSet() {
        return getButtonStyleResourceIdFromBundle() != NOT_SET;
    }

    protected boolean hasCustomButtonTextColor() {
        return getButtonTextColorResourceIdFromBundle() != NOT_SET;
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
