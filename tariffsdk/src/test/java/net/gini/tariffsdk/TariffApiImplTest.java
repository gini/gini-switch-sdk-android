package net.gini.tariffsdk;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.accounts.NetworkErrorException;

import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.jodah.concurrentunit.Waiter;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeoutException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class TariffApiImplTest {

    @Mock
    NetworkCallback<Configuration> mMockConfigurationNetworkCallback;
    @Mock
    private AccessToken mMockAccessToken;
    @Mock
    private AuthenticationService mMockAuthenticationService;
    @Mock
    private ClientParameter mMockClientParameter;
    private HttpUrl mMockUrl;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private Waiter mWaiter;


    @Test
    public void requestConfiguration_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void requestConfiguration_shouldContainAnAuthorizationHeader()
            throws InterruptedException {
        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());

    }

    @Test
    public void requestConfiguration_shouldContainClientDeviceModel()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        final String deviceModel = "Pixel";
        final ClientParameter clientParameter = new ClientParameter(0, null, deviceModel);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains(deviceModel));
    }

    @Test
    public void requestConfiguration_shouldContainClientOsVersion()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        final ClientParameter clientParameter = new ClientParameter(19, null, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains("19"));
    }

    @Test
    public void requestConfiguration_shouldContainClientPlatform()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        final ClientParameter clientParameter = new ClientParameter(0, null, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains("android"));
    }

    @Test
    public void requestConfiguration_shouldContainClientSdkVersion()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        final String sdkVersion = "1.0.1";
        final ClientParameter clientParameter = new ClientParameter(0, sdkVersion, null);
        tariffApi.requestConfiguration(clientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertTrue(request.getPath().contains(ClientParameter.PLATFORM_NAME));
        assertTrue(request.getPath().contains(sdkVersion));
    }

    @Test
    public void requestConfiguration_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);

        tariffApi.requestConfiguration(mMockClientParameter, mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void requestConfiguration_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService, mMockUrl);

        tariffApi.requestConfiguration(mMockClientParameter, new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.assertTrue(e instanceof NetworkErrorException);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final Configuration configuration) {
                mWaiter.fail();
                mWaiter.resume();
            }
        });
        mWaiter.await();
    }

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
        mMockUrl = mServer.url("/");
        mWaiter = new Waiter();

        MockitoAnnotations.initMocks(this);

        when(mMockAccessToken.getToken()).thenReturn("");
        when(mMockAuthenticationService.getUserToken()).thenReturn(mMockAccessToken);

        when(mMockClientParameter.getSdkVersion()).thenReturn("1.2.3");
        when(mMockClientParameter.getDeviceModel()).thenReturn("Nexus 5");
        when(mMockClientParameter.getOsVersion()).thenReturn("25");
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

}