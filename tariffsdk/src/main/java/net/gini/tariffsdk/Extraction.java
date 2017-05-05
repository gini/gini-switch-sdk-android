package net.gini.tariffsdk;

/**
 * The Extraction class that is used to map the received extractions from the Gini API. An
 * extraction is one information that has been found on the photographed images that have been
 * taken by the user. The name field is a describing string of the extraction(e.g. "Zipcode" or
 * "Wholesale Supplier") and the value field is the extracted string that has been found on the
 * image(e.g. "50733" or "Stadtwerke München")
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
     *
     * @return the value of the extraction as String
     */
    public String getValue() {
        return mValue;
    }
}
