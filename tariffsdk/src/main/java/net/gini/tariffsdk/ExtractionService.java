package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.network.Extractions;

interface ExtractionService {

    void changeExtractions(final Extractions extraction);

    Extractions getExtractions();

    void getExtractions(@NonNull final String extractionsUrl);

    int getResultCodeForActivity();
}
