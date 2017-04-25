package net.gini.tariffsdk;

import android.content.Intent;
import android.os.Bundle;

final public class TariffSdkActivity extends TariffSdkBaseActivity {

    private static final int REQUEST_CODE = 191;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getCallingActivity() == null) {
            throw new IllegalStateException("Start this Intent with startActivityForResult()!");
        }

        if(showOnboarding()) {

            //TODO show onboarding
        } else {
            final Intent intent = new IntentFactory(TariffSdk.getSdk())
                    .createTakePictureActivity();
            startActivityForResult(intent, REQUEST_CODE);
        }
        finish();
    }

    private boolean showOnboarding() {
        return false;
    }
}
