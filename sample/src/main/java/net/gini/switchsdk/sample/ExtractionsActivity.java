package net.gini.switchsdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import net.gini.switchsdk.Extractions;
import net.gini.switchsdk.SwitchSdk;

public class ExtractionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extractions);
        final LinearLayout extractionViewContainer = (LinearLayout) findViewById(
                R.id.view_container);

        final SwitchSdk switchSdk = SwitchSdk.getSdk();
        final Extractions extractions = switchSdk.getExtractions();

        extractionViewContainer.removeAllViews();
        final SingleExtractionView companyName = new SingleExtractionView(this, "Vorversorger",
                extractions.getCompanyName());
        extractionViewContainer.addView(companyName);

        final SingleExtractionView counterNumber = new SingleExtractionView(this, "Zählernummer",
                extractions.getEnergyMeterNumber());
        extractionViewContainer.addView(counterNumber);

        findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String feedbackCompanyName = companyName.getValue();
                final String feedbackCounterNumber = counterNumber.getValue();

                Extractions feedbackExtractions = Extractions.newBuilder(extractions).companyName(
                        feedbackCompanyName).energyMeterNumber(feedbackCounterNumber).build();
                switchSdk.provideFeedbakc(feedbackExtractions);
            }
        });
    }

}
