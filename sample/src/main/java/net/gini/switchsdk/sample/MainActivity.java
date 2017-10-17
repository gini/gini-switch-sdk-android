package net.gini.switchsdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import net.gini.switchsdk.SwitchSdk;

import okhttp3.OkHttpClient;


public class MainActivity extends BaseActivity {


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == SwitchSdk.REQUEST_CODE) {
            if (resultCode == SwitchSdk.EXTRACTIONS_AVAILABLE) {
                startActivity(new Intent(this, ExtractionsActivity.class));
            } else {
                Toast.makeText(this, R.string.toast_no_extracions, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final SwitchSdk switchSdk = getSwitchSdk();
                final Intent switchSdkIntent = switchSdk.getSwitchSdkIntent();
                startActivityForResult(switchSdkIntent, SwitchSdk.REQUEST_CODE);
            }
        });

        findViewById(R.id.button_start_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final SwitchSdk switchSdk = getSwitchSdk();
                final Intent switchSdkIntent = switchSdk
                        .setButtonStyleSelector(R.drawable.custom_button)
                        .setButtonTextColor(R.color.white)
                        .setPositiveColor(R.color.custom_positiveColor)
                        .setNegativeColor(R.color.custom_negativeColor)
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
                        .getSwitchSdkIntent();
                startActivityForResult(switchSdkIntent, SwitchSdk.REQUEST_CODE);
            }
        });
    }

    @NonNull
    private SwitchSdk getSwitchSdk() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final SwitchSdk switchSdk = SwitchSdk.init(MainActivity.this, BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                "gini.net", okHttpClient);
        switchSdk.showLogging(true);

        return switchSdk;
    }
}
