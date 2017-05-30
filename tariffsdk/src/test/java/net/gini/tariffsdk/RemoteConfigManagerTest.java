package net.gini.tariffsdk;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RemoteConfigManagerTest {

    @Mock
    Configuration mMockConfiguration;
    @Mock
    NetworkCallback<Configuration> mMockConfigurationNetworkCallback;
    @Mock
    RemoteConfigStore mMockRemoteConfigStore;
    @Mock
    TariffApi mMockTariffApi;
    @Captor
    ArgumentCaptor<NetworkCallback<Configuration>> mNetworkCallbackConfigurationCaptor;
    @Mock
    private Exception mMockException;

    @Test
    public void requestConfigFails_shouldNotStoreThem() {

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi,
                mMockRemoteConfigStore);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockTariffApi).requestConfiguration(mNetworkCallbackConfigurationCaptor.capture());
        mNetworkCallbackConfigurationCaptor.getValue().onError(mMockException);

        verify(mMockRemoteConfigStore, never()).storeMaximalCameraResolution(anyLong());
        verify(mMockRemoteConfigStore, never()).storeUseFlash(anyInt());

    }

    @Test
    public void requestConfigSucceeds_shouldStoreThem() {

        when(mMockConfiguration.getFlashMode()).thenReturn(Integer.MAX_VALUE);
        when(mMockConfiguration.getResolution()).thenReturn(Long.MAX_VALUE);

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi,
                mMockRemoteConfigStore);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockTariffApi).requestConfiguration(mNetworkCallbackConfigurationCaptor.capture());
        mNetworkCallbackConfigurationCaptor.getValue().onSuccess(mMockConfiguration);

        verify(mMockRemoteConfigStore).storeMaximalCameraResolution(Long.MAX_VALUE);
        verify(mMockRemoteConfigStore).storeUseFlash(Integer.MAX_VALUE);

    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

}