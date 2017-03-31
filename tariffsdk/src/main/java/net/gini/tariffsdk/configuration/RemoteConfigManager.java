package net.gini.tariffsdk.configuration;


import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

public class RemoteConfigManager {
    //1. Define version - hardcoded

    //2. Request remote values

    //3. Save them

    //4. Provide them

    private TariffApi mTariffApi;

    private RemoteConfigStore mRemoteConfigStore;

    public RemoteConfigManager(final TariffApi tariffApi,            final RemoteConfigStore remoteConfigStore) {
        mTariffApi = tariffApi;
        mRemoteConfigStore = remoteConfigStore;
    }




    private void requestRemoteConfig() {
        mTariffApi.requestConfiguration(new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final Configuration configuration) {

            }
        });
    }


    private boolean shouldRequestRemoteConfig() {
        //TODO check timestamp or so
        return true;
    }
}
