package net.gini.tariffsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.TariffSdk;
import net.gini.tariffsdk.network.Extractions;


public class MainActivity extends AppCompatActivity {

    private TariffSdk mTariffSdk;

    private TextView mTextView;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == TariffSdk.REQUEST_CODE) {
            if (resultCode == TariffSdk.EXTRACTIONS_AVAILABLE) {
                Extractions extractions = mTariffSdk.getExtractions();
                mTextView.setText(extractions.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        mTariffSdk = TariffSdk.init(this, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,
                "gini.net");

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
                        .setPositiveColor(R.color.custom_positiveColor)
                        .setNegativeColor(R.color.negativeColor)
                        .setAnalyzedText(R.string.analyzedText)
                        .setAnalyzedTextColor(R.color.analyzedTextColor)
                        .setAnalyzedImage(R.drawable.ic_analyzed_image)
                        .setTheme(R.style.CustomTheme)
                        .setExitDialogText(R.string.exit_text)
                        .setAnalyzedTextSize(16)
                        .setReviewDiscardText(R.string.custom_review_discard_text)
                        .setReviewKeepText(R.string.custom_review_keep_text)
                        .setReviewTitleText(R.string.custom_review_text)
                        .setPreviewSuccessText(R.string.custom_preview_success_text)
                        .setPreviewFailedText(R.string.custom_preview_failed_text)
                        .setExtractionEditTextBackgroundColor(R.color.primaryColor)
                        .setExtractionTitleText(R.string.custom_extractions_title)
                        .setExtractionButtonText(R.string.custom_extractions_button_text)
                        .getTariffSdkIntent();
                startActivityForResult(tariffSdkIntent, TariffSdk.REQUEST_CODE);
            }
        });
    }
}
