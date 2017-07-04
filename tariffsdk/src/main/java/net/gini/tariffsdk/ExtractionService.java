package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.network.Extractions;

interface ExtractionService {

    boolean extractionsAvailable();

    void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener);

    Extractions getExtractions();

    int getResultCodeForActivity();

    interface ExtractionListener {
        void onExtractionsReceived();
    }

}
