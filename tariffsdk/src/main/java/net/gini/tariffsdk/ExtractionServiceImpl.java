package net.gini.tariffsdk;


import android.support.annotation.RestrictTo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ExtractionServiceImpl implements ExtractionService {

    private Map<String, String> mExtractions = new HashMap<>();

    ExtractionServiceImpl() {
        //MOCK
        mExtractions.put("Zip Code", "50733");
        mExtractions.put("Wholesale Supplier", "Stadtwerke München");
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public Set<Extraction> getExtractions() {
        Set<Extraction> extractions = new HashSet<>();
        for (String name : mExtractions.keySet()) {
            Extraction extraction = createExtraction(name, mExtractions.get(name));
            extractions.add(extraction);
        }
        return extractions;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public int getResultCodeForActivity() {
        return TariffSdk.EXTRACTIONS_AVAILABLE;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void setExtraction(final Extraction extraction) {
        mExtractions.put(extraction.getName(), extraction.getValue());
    }

    private Extraction createExtraction(final String name, final String value) {
        return new Extraction(name, value);
    }

}
