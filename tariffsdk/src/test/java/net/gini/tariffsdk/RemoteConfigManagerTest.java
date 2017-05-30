package net.gini.tariffsdk;

import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.mockwebserver.MockWebServer;

public class RemoteConfigManagerTest {

    @Mock
    ClientParameter mMockClientParameter;
    @Mock
    Configuration mMockConfiguration;
    @Mock
    NetworkCallback<Configuration> mMockConfigurationNetworkCallback;
    @Mock
    TariffApi mMockTariffApi;
    @Captor
    ArgumentCaptor<NetworkCallback<Configuration>> mNetworkCallbackConfigurationCaptor;
    @Mock
    private Exception mMockException;
    private MockWebServer mServer;

    @Test
    public void requestConfig_shouldContainClientPlatform() {
        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi);
        remoteConfigManager.requestRemoteConfig();

    }

//    @Test
//    public void requestConfigFails_shouldNotStoreThem() {
//
//        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi,
//                mMockClientParameter);
//
//        remoteConfigManager.requestRemoteConfig();
//
//        verify(mMockTariffApi).requestConfiguration(mNetworkCallbackConfigurationCaptor.capture
// ());
//        mNetworkCallbackConfigurationCaptor.getValue().onError(mMockException);
//
//        verify(mMockClientParameter, never()).storeMaximalCameraResolution(anyLong());
//        verify(mMockRemoteConfigStore, never()).storeUseFlash(anyInt());
//
//    }

//    @Test
//    public void requestConfigSucceeds_shouldStoreThem() {
//
//        when(mMockConfiguration.getFlashMode()).thenReturn(Integer.MAX_VALUE);
//        when(mMockConfiguration.getResolution()).thenReturn(Long.MAX_VALUE);
//
//        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi,
//                mMockRemoteConfigStore);
//
//        remoteConfigManager.requestRemoteConfig();
//
//        verify(mMockTariffApi).requestConfiguration(mNetworkCallbackConfigurationCaptor.capture
// ());
//        mNetworkCallbackConfigurationCaptor.getValue().onSuccess(mMockConfiguration);
//
//        verify(mMockRemoteConfigStore).storeMaximalCameraResolution(Long.MAX_VALUE);
//        verify(mMockRemoteConfigStore).storeUseFlash(Integer.MAX_VALUE);
//
//    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

}