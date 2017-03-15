package net.gini.tariffsdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    static final String BUNDLE_EXTRA_RIGHT_INSTANTIATED = "BUNDLE_EXTRA_RIGHT_INSTANTIATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        checkForCorrectUsage();
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
