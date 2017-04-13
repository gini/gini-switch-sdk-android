package net.gini.tariffsdk;

import android.content.Intent;
import android.os.Bundle;

final public class TariffSdkActivity extends TariffSdkBaseActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(showOnboarding()) {

            //TODO show onboarding
        } else {
            final TariffSdkIntentFactory tariffSdkIntentFactory = new TariffSdkIntentFactory(this, getThemeResourceId());
            final Intent intent = tariffSdkIntentFactory.createTakePictureIntent();
            startActivityForResult(intent, REQUEST_CODE);
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
