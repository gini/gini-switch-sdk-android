package net.gini.tariffsdk;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Set;

final public class ExtractionsActivity extends TariffSdkBaseActivity {

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

        final View containerExtractions = findViewById(R.id.container_extractions);
        ViewCompat.animate(containerSplash)
                .translationY(height)
                .setDuration(500)
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

        mExtractionViewContainer = (LinearLayout) findViewById(R.id.view_container);
        mExtractionService = TariffSdk.getSdk().getExtractionService();

        //TODO make this generic so we can set the showing fields via remote config
        setExtractionsInView(mExtractionService.getExtractions());

        final Button viewById = (Button) findViewById(R.id.button_done);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setExtractions();
                setResult(mExtractionService.getResultCodeForActivity());
                finish();
            }
        });
        viewById.setBackgroundResource(getButtonStyleResourceIdFromBundle());
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
            mExtractionViewContainer.addView(view);
        }
    }


}
