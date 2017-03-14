package net.gini.tariffsdk.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.gini.tariffsdk.TariffSdk;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TariffSdk tariffSdk = TariffSdk.getSdkInstance(this);

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent tariffSdkIntent = tariffSdk.getTariffSdkIntent();
                startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if(requestCode == TariffSdk.REQUEST_CODE) {
            //TODO interpret result code
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
