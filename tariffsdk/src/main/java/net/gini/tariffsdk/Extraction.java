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

        if (mName != null ? !mName.equals(that.mName) : that.mName != null) return false;
        return mValue != null ? mValue.equals(that.mValue) : that.mValue == null;

    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mValue != null ? mValue.hashCode() : 0);
        return result;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
