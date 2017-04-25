package net.gini.tariffsdk;


public class Extraction {

    private final String mName;
    private final String mValue;

    Extraction(final String name, final String value) {
        mName = name;
        mValue = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Extraction that = (Extraction) o;

        return mName != null ? mName.equals(that.mName) : that.mName == null;

    }

    @Override
    public int hashCode() {
        return mName != null ? mName.hashCode() : 0;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
