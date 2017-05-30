package net.gini.tariffsdk;


import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

class RemoteConfigManager {

    private Configuration mConfiguration;
    private RemoteConfigStore mRemoteConfigStore;
    private TariffApi mTariffApi;

    RemoteConfigManager(final TariffApi tariffApi,
            final RemoteConfigStore remoteConfigStore) {
        mTariffApi = tariffApi;
        mRemoteConfigStore = remoteConfigStore;
    }

    Configuration getConfiguration() {
        return mConfiguration;
    }

    @VisibleForTesting
    void requestRemoteConfig() {
        mTariffApi.requestConfiguration(new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {
                //TODO
            }

            @Override
            public void onSuccess(final Configuration configuration) {
                mConfiguration = configuration;
                persistConfiguration(mConfiguration);
            }
        });
    }

    private Configuration getConfigurationFromPreferences() {
        final int flashMode = mRemoteConfigStore.getFlashMode();
        final long cameraResolution = mRemoteConfigStore.getMaximalCameraResolution();
        return new Configuration(cameraResolution, flashMode);
    }

    private void persistConfiguration(final Configuration configuration) {
        mRemoteConfigStore.storeMaximalCameraResolution(configuration.getResolution());
        mRemoteConfigStore.storeUseFlash(configuration.getFlashMode());
        //TODO maybe persist timestamp
    }

    private boolean shouldRequestRemoteConfig() {
        //TODO check timestamp or so
        return true;
    }
}
