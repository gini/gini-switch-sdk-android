package net.gini.tariffsdk;


import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

class TariffSdkBaseActivity extends AppCompatActivity {

    @StyleableRes
    private static final int NOT_SET = 0;
    protected static String BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE =
            "BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE";
    protected static String BUNDLE_EXTRA_BUTTON_TEXT_COLOR = "BUNDLE_EXTRA_BUTTON_TEXT_COLOR";
    protected static String BUNDLE_EXTRA_NEGATIVE_COLOR = "BUNDLE_EXTRA_NEGATIVE_COLOR";
    protected static String BUNDLE_EXTRA_POSITIVE_COLOR = "BUNDLE_EXTRA_POSITIVE_COLOR";
    protected static String BUNDLE_EXTRA_THEME = "BUNDLE_EXTRA_THEME";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySettings();
        checkForCorrectUsage();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));
        }
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

    protected int getNegativeColor() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, R.color.negativeColor);
    }

    protected int getPositiveColor() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, R.color.positiveColor);
    }

    protected int getThemeResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_THEME, R.style.GiniTheme);
    }

    protected boolean hasCustomButtonStyleSet() {
        return getButtonStyleResourceIdFromBundle() != NOT_SET;
    }

    protected boolean hasCustomButtonTextColor() {
        return getButtonTextColorResourceIdFromBundle() != NOT_SET;
    }

    private void applySettings() {
        final int theme = getThemeResourceIdFromBundle();
        setTheme(theme);
    }

    private void checkForCorrectUsage() {
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                IntentFactory.BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            throw new IllegalArgumentException(
                    "Do not create this Intent by yourself, use the provided TariffSdk"
                            + ".getTariffSdkIntent() method for it!");
        }

        final TypedValue typedValue = new TypedValue();
        final TypedArray typedArray = obtainStyledAttributes(typedValue.data,
                new int[]{R.attr.windowActionBar,
                        R.attr.windowNoTitle});
        try {
            if (typedArray.getBoolean(0, true) || !typedArray.getBoolean(1, false)) {
                throw new IllegalArgumentException(
                        "Your Style should extend the GiniTheme! Check the documentation for more"
                                + " information about it.");
            }
        } finally {
            typedArray.recycle();
        }
    }

}
