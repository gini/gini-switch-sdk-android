package net.gini.tariffsdk.configuration.models;


public class Configuration {

    private final long mResolution;

    public Configuration(final long resolution) {

        mResolution = resolution;
    }

    public long getResolution() {
        return mResolution;
    }
}
