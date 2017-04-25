package net.gini.tariffsdk;


import java.util.ArrayList;
import java.util.List;

class ExtractionServiceImpl implements ExtractionService {

    private List<Extractions> mExtractions = new ArrayList<>();

    @Override
    public List<Extractions> getExtractions() {
        return mExtractions;
    }
}
