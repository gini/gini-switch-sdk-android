package net.gini.switchsdk;


import android.support.annotation.NonNull;

import net.gini.switchsdk.network.Extractions;

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
