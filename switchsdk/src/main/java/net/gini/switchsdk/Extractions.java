package net.gini.switchsdk;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;


/**
 * The Extraction class that is used to get the received extractions from the Gini API. An
 * extraction is one piece of information that has been found on the photographed images that
 * have been
 * taken by the user. The corresponding getters can be used to get the specific extraction.
 * The given extractions are:
 * companyName, represents the wholesale supplier, e.g. "Stadtwerke München"
 * consumptionMeterNumber, represents the found number of the consumption meter, e.g. "L4711"
 * consumptionValue, represents the found consumption amount, e.g. 47.11
 * consumptionUnit, represents the unit used with the consumption value, e.g. kWh
 */
public class Extractions {
    private final String mCompanyName;
    private final String mConsumptionUnit;
    private final double mConsumptionValue;
    private final String mEnergyMeterNumber;
    private final String mSelf;

    private Extractions(final Builder builder) {
        mCompanyName = builder.mCompanyName;
        mConsumptionUnit = builder.mConsumptionUnit;
        mConsumptionValue = builder.mConsumptionValue;
        mEnergyMeterNumber = builder.mEnergyMeterNumber;
        mSelf = builder.mSelf;
    }

    @Override
    public String toString() {
        return "Extractions{" +
                "mCompanyName='" + mCompanyName + '\'' +
                ", mConsumptionUnit='" + mConsumptionUnit + '\'' +
                ", mConsumptionValue=" + mConsumptionValue +
                ", mEnergyMeterNumber='" + mEnergyMeterNumber + '\'' +
                '}';
    }

    public static Builder newBuilder(@NonNull final Extractions extractions) {
        Builder builder = new Builder();
        builder.mCompanyName = extractions.mCompanyName;
        builder.mConsumptionUnit = extractions.mConsumptionUnit;
        builder.mConsumptionValue = extractions.mConsumptionValue;
        builder.mEnergyMeterNumber = extractions.mEnergyMeterNumber;
        builder.mSelf = extractions.mSelf;
        return builder;
    }

    /**
     * Returns the found name of the wholesale supplier as a string. If nothing has been found the
     * string is empty.
     *
     * @return the wholesale supplier as a string, or an empty string
     */
    @NonNull
    public String getCompanyName() {
        return mCompanyName;
    }

    /**
     * Returns the found consumption unit as a string. This usually comes with {@link
     * Extractions#getConsumptionValue()} together.
     *
     * @return the consumption unit as a string, or an empty string
     */
    @NonNull
    public String getConsumptionUnit() {
        return mConsumptionUnit;
    }

    /**
     * Returns the found consumption value as a double. This usually comes with {@link
     * Extractions#getConsumptionUnit()} together.
     *
     * @return the consumption unit as a double, or Double.NaN.
     */
    public double getConsumptionValue() {
        return mConsumptionValue;
    }

    /**
     * The found energy meter number as a string. If nothing has been found the string is empty.
     *
     * @return energy meter number as a string, or an empty string.
     */
    @NonNull
    public String getEnergyMeterNumber() {
        return mEnergyMeterNumber;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public String getSelf() {
        return mSelf;
    }

    /**
     * {@code Extractions} builder static inner class.
     */
    public static final class Builder {
        private String mCompanyName;
        private String mConsumptionUnit;
        private double mConsumptionValue;
        private String mEnergyMeterNumber;
        private String mSelf;

        Builder() {
        }

        /**
         * Returns a {@code Extractions} built from the parameters previously set.
         *
         * @return a {@code Extractions} built with parameters of this {@code Extractions.Builder}
         */
        public Extractions build() {
            return new Extractions(this);
        }

        /**
         * Sets the {@code mCompanyName} and returns a reference to this Builder so that the methods
         * can be chained together.
         *
         * @param companyName the {@code mCompanyName} to set
         * @return a reference to this Builder
         */
        public Builder companyName(@NonNull final String companyName) {
            mCompanyName = companyName;
            return this;
        }

        /**
         * Sets the {@code mConsumptionUnit} and returns a reference to this Builder so that the
         * methods can be chained together.
         *
         * @param consumptionUnit the {@code mConsumptionUnit} to set
         * @return a reference to this Builder
         */
        public Builder consumptionUnit(@NonNull final String consumptionUnit) {
            mConsumptionUnit = consumptionUnit;
            return this;
        }

        /**
         * Sets the {@code mConsumptionValue} and returns a reference to this Builder so that the
         * methods can be chained together.
         *
         * @param consumptionValue the {@code mConsumptionValue} to set
         * @return a reference to this Builder
         */
        public Builder consumptionValue(final double consumptionValue) {
            mConsumptionValue = consumptionValue;
            return this;
        }

        /**
         * Sets the {@code mEnergyMeterNumber} and returns a reference to this Builder so that the
         * methods can be chained together.
         *
         * @param energyMeterNumber the {@code mEnergyMeterNumber} to set
         * @return a reference to this Builder
         */
        public Builder energyMeterNumber(@NonNull final String energyMeterNumber) {
            mEnergyMeterNumber = energyMeterNumber;
            return this;
        }

        Builder selfLink(@NonNull final String self) {
            mSelf = self;
            return this;
        }
    }
}
