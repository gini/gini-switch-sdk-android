package net.gini.switchsdk.network;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;


/**
 * The Extraction class that is used to get the received extractions from the Gini API. An
 * extraction is one information that has been found on the photographed images that have been
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

    public Extractions(@NonNull final String self, @NonNull final String companyName,
            @NonNull final String energyMeterNumber,
            final double consumptionValue,
            @NonNull final String consumptionUnit) {
        mSelf = self;
        mCompanyName = companyName;
        mEnergyMeterNumber = energyMeterNumber;
        mConsumptionUnit = consumptionUnit;
        mConsumptionValue = consumptionValue;
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

    /**
     * Returns the found name of the  wholesale supplier as a string. If nothing has been found the
     * string is empty.
     *
     * @return the  wholesale supplier as a string, or empty.
     */
    @NonNull
    public String getCompanyName() {
        return mCompanyName;
    }

    /**
     * Returns the found consumption unit as a string. This usually comes with with {@link
     * Extractions#getConsumptionValue()} together.
     *
     * @return the consumption unit as a string, or empty.
     */
    @NonNull
    public String getConsumptionUnit() {
        return mConsumptionUnit;
    }

    /**
     * Returns the found consumption value as a double. This usually comes with with {@link
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
     * @return energy meter number as a string, or empty.
     */
    @NonNull
    public String getEnergyMeterNumber() {
        return mEnergyMeterNumber;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public String getSelf() {
        return mSelf;
    }
}
