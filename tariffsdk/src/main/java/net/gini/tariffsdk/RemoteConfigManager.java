package net.gini.tariffsdk;


import net.gini.tariffsdk.configuration.models.ClientInformation;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.configuration.models.FlashMode;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

class RemoteConfigManager {

    private final ClientInformation mClientInformation;
    private Configuration mConfiguration;
    private TariffApi mTariffApi;

    RemoteConfigManager(final TariffApi tariffApi) {
        mTariffApi = tariffApi;
        mClientInformation = new ClientInformation(getOsVersion(), getSdkVersion(),
                getDeviceModel());
    }

    FlashMode getFlashMode() {
        return mConfiguration != null ? mConfiguration.getFlashMode() : FlashMode.ON;
    }

    void requestRemoteConfig() {
        mTariffApi.requestConfiguration(mClientInformation, new NetworkCallback<Configuration>() {
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
