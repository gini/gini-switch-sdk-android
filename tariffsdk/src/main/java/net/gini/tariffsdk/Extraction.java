package net.gini.tariffsdk;

/**
 * The extraction class that is used to map extractions.
 */
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

    @Override
    public String toString() {
        return "Extraction{" +
                "mName='" + mName + '\'' +
                ", mValue='" + mValue + '\'' +
                '}';
    }

    /**
     * Use this to retrieve the name of the extraction.
     *
     * @return the name of the extraction as String
     */
    public String getName() {
        return mName;
    }

    /**
     * Use this to retrieve the value of the extraction.
     * @return the value of the extraction as String
     */
    public String getValue() {
        return mValue;
    }
}
