package net.gini.tariffsdk.remoteconfig;


import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.RemoteConfigApi;

public class RemoteConfig {
    //1. Define version - hardcoded

    //2. Request remote values

    //3. Save them

    //4. Provide them

    private RemoteConfigApi mRemoteConfigApi;

    private RemoteConfigStore mRemoteConfigStore;

    public RemoteConfig(final RemoteConfigApi remoteConfigApi,
            final RemoteConfigStore remoteConfigStore) {
        mRemoteConfigApi = remoteConfigApi;
        mRemoteConfigStore = remoteConfigStore;
    }


    private void requestRemoteConfig() {
        mRemoteConfigApi.requestRemoteConfig(new NetworkCallback<RemoteValues>() {
            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final RemoteValues remoteValues) {
                processRemoteValues(remoteValues);
            }
        });
    }

    private void processRemoteValues(final RemoteValues remoteValues) {
        mRemoteConfigStore.storeMaximalCameraResolution(remoteValues.getResolution());
        mRemoteConfigStore.storeUseFlash(remoteValues.isUseFlash());
    }

    public boolean flashOn() {
        return mRemoteConfigStore.useFlash();
    }

    public long getMaximalCameraResolution() {
        return mRemoteConfigStore.getMaximalCameraResolution();
    }
}
