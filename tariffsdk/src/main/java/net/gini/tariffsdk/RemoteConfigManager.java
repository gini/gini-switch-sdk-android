package net.gini.tariffsdk;


import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

class RemoteConfigManager {

    private final ClientParameter mClientParameter;
    private Configuration mConfiguration;
    private TariffApi mTariffApi;

    RemoteConfigManager(final TariffApi tariffApi) {
        mTariffApi = tariffApi;
        mClientParameter = new ClientParameter(getOsVersion(), getSdkVersion(), getDeviceModel());
    }

    @VisibleForTesting
    void requestRemoteConfig() {
        mTariffApi.requestConfiguration(mClientParameter, new NetworkCallback<Configuration>() {
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
