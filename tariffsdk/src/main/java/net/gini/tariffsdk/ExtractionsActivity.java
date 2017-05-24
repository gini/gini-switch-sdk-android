package net.gini.tariffsdk;


import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Set;

final public class ExtractionsActivity extends TariffSdkBaseActivity {

    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE = "BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT = "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE";
    static final String BUNDLE_EXTRA_BUTTON_TEXT = "BUNDLE_EXTRA_BUTTON_TEXT";
    static final String BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR =
            "BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR";
    static final String BUNDLE_EXTRA_EDIT_TEXT_COLOR = "BUNDLE_EXTRA_EDIT_TEXT_COLOR";
    static final String BUNDLE_EXTRA_HINT_COLOR = "BUNDLE_EXTRA_HINT_COLOR";
    static final String BUNDLE_EXTRA_LINE_COLOR = "BUNDLE_EXTRA_LINE_COLOR";
    static final String BUNDLE_EXTRA_TITLE_TEXT = "BUNDLE_EXTRA_TITLE_TEXT";
    private ExtractionService mExtractionService;
    private LinearLayout mExtractionViewContainer;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_extractions);
        View containerSplash = findViewById(R.id.container_splash);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        setUpAnalyzedCompletedScreen(containerSplash, height);

        mExtractionViewContainer = (LinearLayout) findViewById(R.id.view_container);
        mExtractionService = TariffSdk.getSdk().getExtractionService();

        //TODO make this generic so we can set the showing fields via remote config
        setExtractionsInView(mExtractionService.getExtractions());

        final Button confirmButton = (Button) findViewById(R.id.button_done);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setExtractions();
                setResult(mExtractionService.getResultCodeForActivity());
                finish();
            }
        });

        styleConfirmButton(confirmButton);

        TextView title = (TextView) findViewById(R.id.extractions_title);
        title.setText(getTitleTextIdFromBundle());
    }

    private int getAnalyzedImageFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE,
                R.drawable.ic_check_circle);
    }

    private int getAnalyzedTextColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR,
                R.color.primaryText);
    }

    private int getAnalyzedTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, R.string.analyzed_text);
    }

    private int getAnalyzedTextSizeFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE,
                getResources().getInteger(R.integer.analyzed_text_size));
    }

    private int getButtonTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT, R.string.button_extractions);
    }

    private int getEditTextBackgroundColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR,
                R.color.secondaryColor);
    }

    private int getEditTextColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, R.color.primaryText);
    }

    private int getHintColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_HINT_COLOR, R.color.primaryText);
    }

    private int getLineColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_LINE_COLOR, R.color.primaryText);
    }

    private int getTitleTextIdFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_TITLE_TEXT, R.string.extractions_title);
    }

    private void setExtractions() {
        for (int i = 0; i < mExtractionViewContainer.getChildCount(); i++) {
            Extraction extraction = ((SingleExtractionView) mExtractionViewContainer.getChildAt(
                    i)).getExtraction();
            mExtractionService.setExtraction(extraction);
        }
    }

    private void setExtractionsInView(Set<Extraction> extractionSet) {
        mExtractionViewContainer.removeAllViews();
        for (Extraction extraction : extractionSet) {
            SingleExtractionView view = new SingleExtractionView(this, extraction);
            view.setTextColor(getEditTextColorFromBundle());

            view.setHintColor(getHintColorFromBundle());
            view.setLineColor(getLineColorFromBundle());
            view.setBackgroundColor(getEditTextBackgroundColorFromBundle());
            view.setNegativeColor(getNegativeColor());
            mExtractionViewContainer.addView(view);
        }
    }

    private void setUpAnalyzedCompletedScreen(final View containerSplash, final int height) {
        final View containerExtractions = findViewById(R.id.container_extractions);
        ViewCompat.animate(containerSplash)
                .translationY(height)
                .setDuration(250)
                .setStartDelay(3000)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationCancel(final View view) {
                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        containerExtractions.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationStart(final View view) {
                    }
                })
                .start();

        ImageView analyzedImage = (ImageView) findViewById(R.id.analyzed_image);
        analyzedImage.setImageDrawable(ContextCompat.getDrawable(this, getAnalyzedImageFromBundle
                ()));

        TextView analyzedText = (TextView) findViewById(R.id.analyzed_text);
        analyzedText.setText(getAnalyzedTextFromBundle());
        analyzedText.setTextColor(ContextCompat.getColor(this, getAnalyzedTextColorFromBundle()));
        analyzedText.setTextSize(COMPLEX_UNIT_SP, getAnalyzedTextSizeFromBundle());
    }

    private void styleConfirmButton(final Button confirmButton) {
        if (hasCustomButtonStyleSet()) {
            confirmButton.setBackgroundResource(getButtonStyleResourceIdFromBundle());
        } else {
            confirmButton.setBackgroundColor(ContextCompat.getColor(this, R.color.button_confirm));
        }
        int customButtonTextColor =
                hasCustomButtonTextColor() ? getButtonTextColorResourceIdFromBundle()
                        : R.color.primaryText;
        int textColor = ContextCompat.getColor(this, customButtonTextColor);
        confirmButton.setTextColor(textColor);
        confirmButton.setText(getButtonTextFromBundle());
    }
}
