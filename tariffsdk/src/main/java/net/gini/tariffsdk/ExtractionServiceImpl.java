package net.gini.tariffsdk;


import java.util.HashSet;
import java.util.Set;

class ExtractionServiceImpl implements ExtractionService {

    private Set<Extraction> mExtractions = new HashSet<>();

    ExtractionServiceImpl() {
        //MOCK
        mExtractions.add(new Extraction("Zip Code", "50733"));
        mExtractions.add(new Extraction("Wholesale Supplier", "Stadtwerke München"));
    }

    @Override
    public Set<Extraction> getExtractions() {
        return mExtractions;
    }

    @Override
    public int getResultCodeForActivity() {
        return TariffSdk.EXTRACTIONS_AVAILABLE;
    }

    @Override
    public void setExtraction(final Extraction extraction) {
        if (mExtractions.contains(extraction)) {
            mExtractions.remove(extraction);
        }
        mExtractions.add(extraction);
    }

}
