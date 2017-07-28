package net.gini.switchsdk.configuration.models;


public class Configuration {

    public static final String FLASH_MODE = "flashMode";
    private final FlashMode mFlashMode;

    public Configuration(final FlashMode flashMode) {

        mFlashMode = flashMode;
    }

    public FlashMode getFlashMode() {
        return mFlashMode;
    }
}
