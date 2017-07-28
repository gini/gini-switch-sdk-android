package net.gini.switchsdk;

import static junit.framework.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.switchsdk.configuration.models.ClientInformation;
import net.gini.switchsdk.configuration.models.Configuration;
import net.gini.switchsdk.configuration.models.FlashMode;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.SwitchApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class RemoteConfigManagerTest {

    @Mock
    ClientInformation mMockClientInformation;
    @Mock
    Configuration mMockConfiguration;
    @Mock
    NetworkCallback<Configuration> mMockConfigurationNetworkCallback;
    @Mock
    SwitchApi mMockSwitchApi;
    @Captor
    ArgumentCaptor<NetworkCallback<Configuration>> mNetworkCallbackConfigurationCaptor;
    @Mock
    private Exception mMockException;

    @Test
    public void requestConfigFails_shouldGiveDefaultValues() {

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockSwitchApi);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockSwitchApi).requestConfiguration(any(ClientInformation.class),
                mNetworkCallbackConfigurationCaptor.capture());
        mNetworkCallbackConfigurationCaptor.getValue().onError(mMockException);

        FlashMode flashmode = remoteConfigManager.getFlashMode();

        assertEquals(FlashMode.ON, flashmode);

    }

    @Test
    public void requestConfigSucceeds_shouldStoreThem() {

        when(mMockConfiguration.getFlashMode()).thenReturn(FlashMode.AUTO);

        RemoteConfigManager remoteConfigManager = new RemoteConfigManager(mMockSwitchApi);

        remoteConfigManager.requestRemoteConfig();

        verify(mMockSwitchApi).requestConfiguration(any(ClientInformation.class),
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