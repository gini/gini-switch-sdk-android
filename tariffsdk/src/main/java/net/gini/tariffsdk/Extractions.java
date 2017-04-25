package net.gini.tariffsdk;


public class Extractions {
    private final String mName;

    private final String mValue;

    Extractions(final String name, final String value) {
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
