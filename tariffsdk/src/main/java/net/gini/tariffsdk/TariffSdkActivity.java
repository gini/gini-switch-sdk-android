package net.gini.tariffsdk;

import android.content.Intent;
import android.os.Bundle;

import net.gini.tariffsdk.takepictures.TakePictureActivity;

final public class TariffSdkActivity extends TariffSdkBaseActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkForCorrectUsage();

        if(showOnboarding()) {

            //TODO show onboarding
        } else {
            final Intent intent = TakePictureActivity.newIntent(this, getThemeResourceId());
            startActivityForResult(intent, REQUEST_CODE);
        }
        finish();
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

    private boolean showOnboarding() {
        return false;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            finishAffinity();
        }
    }
}
