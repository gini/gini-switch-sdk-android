package net.gini.tariffsdk;


import java.util.Set;

interface ExtractionService {

    Set<Extraction> getExtractions();

    int getResultCodeForActivity();

    void setExtraction(final Extraction extraction);
}
