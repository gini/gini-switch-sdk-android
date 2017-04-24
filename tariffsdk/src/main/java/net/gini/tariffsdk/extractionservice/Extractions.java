package net.gini.tariffsdk.extractionservice;


public class Extractions {
    private final String mName;

    private final String mValue;

    public Extractions(final String name, final String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
