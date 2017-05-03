package net.gini.tariffsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.gini.tariffsdk.TariffSdk;


public class MainActivity extends AppCompatActivity {

    private TariffSdk mTariffSdk;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == TariffSdk.REQUEST_CODE) {
            if (resultCode == TariffSdk.EXTRACTIONS_AVAILABLE) {
                mTariffSdk.getExtractions();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTariffSdk = TariffSdk.init(this, "clientId", "clientPw");

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent tariffSdkIntent = mTariffSdk.getTariffSdkIntent();
                startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);
            }
        });

        findViewById(R.id.button_start_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent tariffSdkIntent = mTariffSdk
                        .withTheme(R.style.SpecialTheme)
                        .getTariffSdkIntent();
                startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);
            }
        });
    }
}
