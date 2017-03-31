package net.gini.tariffsdk.configuration;


public class RemoteValues {
    private final boolean mUseFlash;

    private final long mResolution;

    public boolean isUseFlash() {
        return mUseFlash;
    }

    public long getResolution() {
        return mResolution;
    }

    public RemoteValues(final boolean useFlash, final long resolution) {
        mUseFlash = useFlash;
        mResolution = resolution;
    }
}
