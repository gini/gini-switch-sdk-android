package net.gini.tariffsdk.configuration.models;


public class Configuration {

    private final long mResolution;

    private final int mFlashMode;

    public Configuration(final long resolution, final int flashMode) {

        mResolution = resolution;
        mFlashMode = flashMode;
    }

    public int getFlashMode() {
        return mFlashMode;
    }

    public long getResolution() {
        return mResolution;
    }
}
