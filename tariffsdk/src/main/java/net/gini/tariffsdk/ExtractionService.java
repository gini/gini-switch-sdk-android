package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.network.Extractions;

interface ExtractionService {

    Extractions fetchExtractions();

    void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener);

    int getResultCodeForActivity();

    interface ExtractionListener {
        void onExtractionsReceived();
    }

}
