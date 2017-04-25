package net.gini.tariffsdk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Set;

final public class ExtractionsActivity extends TariffSdkBaseActivity {

    private ExtractionService mExtractionService;
    private Set<Extraction> mExtractionSet;
    private LinearLayout mExtractionViewContainer;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_extractions);
        mExtractionViewContainer = (LinearLayout) findViewById(R.id.view_container);

        mExtractionService = TariffSdk.getSdk().getExtractionService();

        //TODO make this generic so we can set the showing fields via remote config
        setExtractionsInView(mExtractionService.getExtractions());

        findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setExtractions();
                setResult(mExtractionService.getResultCodeForActivity());
                finish();
            }
        });
    }

    private void setExtractions() {
        for (int i = 0; i < mExtractionViewContainer.getChildCount(); i++) {
            Extraction extraction = ((SingleExtractionView) mExtractionViewContainer.getChildAt(
                    i)).getExtraction();
            mExtractionService.setExtraction(extraction);
        }
    }

    private void setExtractionsInView(Set<Extraction> extractionSet) {
        mExtractionSet = extractionSet;
        mExtractionViewContainer.removeAllViews();
        for (Extraction extraction : extractionSet) {
            SingleExtractionView view = new SingleExtractionView(this, extraction);
            mExtractionViewContainer.addView(view);
        }
    }


}
