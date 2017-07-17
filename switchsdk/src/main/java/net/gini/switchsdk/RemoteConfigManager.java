package net.gini.switchsdk;


import net.gini.switchsdk.configuration.models.ClientInformation;
import net.gini.switchsdk.configuration.models.Configuration;
import net.gini.switchsdk.configuration.models.FlashMode;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.SwitchApi;

class RemoteConfigManager {

    private final ClientInformation mClientInformation;
    private Configuration mConfiguration;
    private SwitchApi mSwitchApi;

    RemoteConfigManager(final SwitchApi switchApi) {
        mSwitchApi = switchApi;
        mClientInformation = new ClientInformation(getOsVersion(), getSdkVersion(),
                getDeviceModel());
    }

    FlashMode getFlashMode() {
        return mConfiguration != null ? mConfiguration.getFlashMode() : FlashMode.ON;
    }

    void requestRemoteConfig() {
        mSwitchApi.requestConfiguration(mClientInformation, new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {
                //TODO
            }

            @Override
            public void onSuccess(final Configuration configuration) {
                mConfiguration = configuration;
            }
        });
    }

    private String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    private int getOsVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    private String getSdkVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
