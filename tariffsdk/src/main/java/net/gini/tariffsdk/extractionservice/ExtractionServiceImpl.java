package net.gini.tariffsdk.extractionservice;


import java.util.ArrayList;
import java.util.List;

public class ExtractionServiceImpl implements ExtractionService {

    private List<Extractions> mExtractions = new ArrayList<>();

    @Override
    public List<Extractions> getExtractions() {
        return mExtractions;
    }
}
