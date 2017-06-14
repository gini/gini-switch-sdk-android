package net.gini.tariffsdk.network;


public class Extractions {
    private final String mCompanyName;
    private final String mConsumptionUnit;
    private final double mConsumptionValue;
    private final String mEnergyMeterNumber;
    private final String mSelf;

    public Extractions(final String self, final String companyName, final String energyMeterNumber,
            final double consumptionValue,
            final String consumptionUnit) {
        mSelf = self;
        mCompanyName = companyName;
        mEnergyMeterNumber = energyMeterNumber;
        mConsumptionUnit = consumptionUnit;
        mConsumptionValue = consumptionValue;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getConsumptionUnit() {
        return mConsumptionUnit;
    }

    public double getConsumptionValue() {
        return mConsumptionValue;
    }

    public String getEnergyMeterNumber() {
        return mEnergyMeterNumber;
    }

    public String getSelf() {
        return mSelf;
    }
}
