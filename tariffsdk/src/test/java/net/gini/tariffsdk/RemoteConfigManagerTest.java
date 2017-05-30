package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.configuration.models.FlashMode;
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

    @Test
    public void requestConfigFails_shouldGiveDefaultValues() {

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockTariffApi).requestConfiguration(any(ClientParameter.class),
                mNetworkCallbackConfigurationCaptor.capture());
        mNetworkCallbackConfigurationCaptor.getValue().onError(mMockException);

        FlashMode flashmode = remoteConfigManager.getFlashMode();

        assertEquals(FlashMode.ON, flashmode);

    }

    @Test
    public void requestConfigSucceeds_shouldStoreThem() {

        when(mMockConfiguration.getFlashMode()).thenReturn(FlashMode.AUTO);

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockTariffApi);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockTariffApi).requestConfiguration(any(ClientParameter.class),
                mNetworkCallbackConfigurationCaptor.capture());
        mNetworkCallbackConfigurationCaptor.getValue().onSuccess(mMockConfiguration);

        FlashMode flashmode = remoteConfigManager.getFlashMode();

        assertEquals(FlashMode.AUTO, flashmode);

    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

}