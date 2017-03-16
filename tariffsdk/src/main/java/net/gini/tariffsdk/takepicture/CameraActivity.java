package net.gini.tariffsdk.takepicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.gini.tariffsdk.R;

final public class CameraActivity extends AppCompatActivity {

    private static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED =
            TariffSdkIntentCreator.BUNDLE_EXTRA_RIGHT_INSTANTIATED;

    private static final String  BUNDLE_EXTRA_THEME = TariffSdkIntentCreator.BUNDLE_EXTRA_THEME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCorrectUsage();
        applySettings();

        setContentView(R.layout.activity_camera);
    }

    private void applySettings() {
        final int theme = getIntent().getIntExtra(BUNDLE_EXTRA_THEME, -1);
        if(theme != -1) {
            setTheme(theme);
        }
    }

    private void checkForCorrectUsage() {
        if (getCallingActivity() == null) {
            //TODO
            throw new RuntimeException("WRONG!");
        }
        if (getIntent().getExtras() == null || !getIntent().getBooleanExtra(
                BUNDLE_EXTRA_RIGHT_INSTANTIATED, false)) {
            //TODO
            throw new RuntimeException("WRONG!");
        }
    }
}
