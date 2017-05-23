package net.gini.tariffsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.gini.tariffsdk.Extraction;
import net.gini.tariffsdk.TariffSdk;

import java.util.Arrays;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private TariffSdk mTariffSdk;

    private TextView mTextView;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == TariffSdk.REQUEST_CODE) {
            if (resultCode == TariffSdk.EXTRACTIONS_AVAILABLE) {
                Set<Extraction> extractions = mTariffSdk.getExtractions();
                mTextView.setText(Arrays.toString(extractions.toArray()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        mTariffSdk = TariffSdk.init(this, "clientId", "clientPw", "gini.net");

        Button viewById = (Button) findViewById(R.id.button_start);
        viewById.setOnClickListener(new View.OnClickListener() {
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
                        .setButtonStyleSelector(R.drawable.custom_button)
                        .setButtonTextColor(R.color.white)
                        .setPositiveColor(R.color.positiveColor)
                        .setNegativeColor(R.color.negativeColor)
                        .setAnalyzedText(R.string.analyzedText)
                        .setAnalyzedTextColor(R.color.analyzedTextColor)
                        .setAnalyzedImage(R.drawable.ic_analyzed_image)
                        .setTheme(R.style.CustomTheme)
                        .setExitDialogText(R.string.exit_text)
                        .setAnalyzedTextSize(16)
                        .setExtractionEditTextBackgroundColor(R.color.primaryColor)
                        .setExtractionTitleText(R.string.custom_extractions_title)
                        .setExtractionButtonText(R.string.custom_extractions_button_text)
                        .getTariffSdkIntent();
                startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);
            }
        });
    }
}
