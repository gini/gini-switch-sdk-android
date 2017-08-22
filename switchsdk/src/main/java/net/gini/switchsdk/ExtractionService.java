package net.gini.switchsdk;


import android.support.annotation.NonNull;

interface ExtractionService {

    void cleanup();

    boolean extractionsAvailable();

    void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener);

    Extractions getExtractions();

    int getResultCodeForActivity();

    void sendExtractions(@NonNull final Extractions extractions);

    interface ExtractionListener {
        void onExtractionsReceived();
    }

}
