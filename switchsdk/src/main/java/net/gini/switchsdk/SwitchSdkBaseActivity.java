package net.gini.switchsdk;


import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.gini.switchsdk.utils.ExitDialogFragment;

abstract class SwitchSdkBaseActivity extends AppCompatActivity implements
        ExitDialogFragment.ExitDialogListener {

    protected static final String BUNDLE_EXTRA_EXIT_DIALOG_TEXT = "BUNDLE_EXTRA_EXIT_DIALOG_TEXT";
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.switch_base_menu, menu);
        return true;
    }

    @Override
    public void onNegative() {
        //TODO track etc.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_item_cancel) {
            showAbortDialog();
            return true;
        } else if (i == R.id.menu_item_help) {
            showHelpDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPositive() {
        //TODO track etc.
        SwitchSdk.getSdk().cleanUp();
        finishAffinity();
    }

    protected void colorToolbar(final Toolbar toolbar) {
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
                drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), getAccentColor());
                toolbar.setOverflowIcon(drawable);
        }
    }

    protected int getAccentColor() {
        final TypedValue typedValue = new TypedValue();
        final TypedArray typedArray = obtainStyledAttributes(typedValue.data,
                new int[]{R.attr.colorAccent});
        int color;
        try {
            color = typedArray.getColor(0, 0);
        } finally {
            typedArray.recycle();
        }
        return color;
    }

    protected int getButtonStyleResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, NOT_SET);
    }

    protected int getButtonTextColorResourceIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, NOT_SET);
    }

    protected int getExitDialogText() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, R.string.exit_dialog_text);
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

    protected void showAbortDialog() {
        DialogFragment dialog = ExitDialogFragment.newInstance(getExitDialogText());
        dialog.show(getFragmentManager(), "ExitDialogFragment");
    }

    abstract protected void showHelpDialog();

    private void applySettings() {
        final int theme = getThemeResourceIdFromBundle();
        setTheme(theme);
    }

    private void checkForCorrectUsage() {
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                IntentFactory.BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            throw new IllegalArgumentException(
                    "Do not create this Intent by yourself, use the provided SwitchSdk"
                            + ".getSwitchSdkIntent() method for it!");
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
