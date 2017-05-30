package net.gini.tariffsdk.configuration.models;


public class ClientParameter {

    public static final String DEVICE_NAME = "device";
    public static final String OSVERSION_NAME = "osVersion";
    public static final String PLATFORM_NAME = "platform";
    public static final String SDKVERSION_NAME = "sdkVersion";
    private final String mDeviceModel;
    private final String mOsVersion;
    private final String mPlatformName;
    private final String mSdkVersion;

    public ClientParameter(final int osVersion, final String sdkVersion, final String deviceModel) {
        mPlatformName = "android";
        mOsVersion = Integer.toString(osVersion);
        mSdkVersion = sdkVersion;
        mDeviceModel = deviceModel;
    }

    public String getDeviceModel() {
        return mDeviceModel;
    }

    public String getOsVersion() {
        return mOsVersion;
    }

    public String getPlatformName() {
        return mPlatformName;
    }

    public String getSdkVersion() {
        return mSdkVersion;
    }

}
