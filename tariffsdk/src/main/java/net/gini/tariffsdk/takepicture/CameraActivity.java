package net.gini.tariffsdk.takepicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.gini.tariffsdk.R;

final public class CameraActivity extends AppCompatActivity {

    private static final int NOT_SET = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCorrectUsage();
        applySettings();

        setContentView(R.layout.activity_camera);
    }

    private void applySettings() {
        final int theme = getIntent().getIntExtra(TariffSdkIntentFactory.BUNDLE_EXTRA_THEME, NOT_SET);
        if(theme != NOT_SET) {
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
