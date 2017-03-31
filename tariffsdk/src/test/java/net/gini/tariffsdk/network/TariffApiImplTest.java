package net.gini.tariffsdk.network;

import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.accounts.NetworkErrorException;

import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.jodah.concurrentunit.Waiter;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeoutException;

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
    private String mMockUrl;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private Waiter mWaiter;

    @Test
    public void requestConfiguration_responseShouldContainResolution()
            throws InterruptedException, TimeoutException {

        final long resolution = Long.MAX_VALUE;
        final MockResponse mockResponse = new MockResponse().setBody(
                "{\"resolution\":" + resolution + "}");
        mServer.enqueue(mockResponse);
        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService);
        tariffApi.mTariffApiUrl = mMockUrl;

        tariffApi.requestConfiguration(new NetworkCallback<Configuration>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final Configuration configuration) {
                mWaiter.assertEquals(resolution, configuration.getResolution());
                mWaiter.resume();
            }
        });
        mWaiter.await();
    }

    @Test
    public void requestConfiguration_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService);
        tariffApi.mTariffApiUrl = mMockUrl;

        tariffApi.requestConfiguration(mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void requestConfiguration_shouldContainAnAuthorizationHeader()
            throws InterruptedException {
        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService);
        tariffApi.mTariffApiUrl = mMockUrl;

        tariffApi.requestConfiguration(mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        String authorizationHeader = request.getHeader("Authorization");
        assertFalse(authorizationHeader.isEmpty());

    }

    @Test
    public void requestConfiguration_shouldContainTheBearerTokenAsAuthorization()
            throws InterruptedException {

        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService);
        tariffApi.mTariffApiUrl = mMockUrl;
        final String bearerToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";
        when(mMockAccessToken.getToken()).thenReturn(bearerToken);

        tariffApi.requestConfiguration(mMockConfigurationNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + bearerToken, request.getHeader("Authorization"));
    }

    @Test
    public void requestConfiguration_wasNotSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final TariffApiImpl tariffApi = new TariffApiImpl(mOkHttpClient,
                mMockAuthenticationService);
        tariffApi.mTariffApiUrl = mMockUrl;

        tariffApi.requestConfiguration(new NetworkCallback<Configuration>() {
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
        mMockUrl = mServer.url("/").toString();
        mWaiter = new Waiter();

        MockitoAnnotations.initMocks(this);

        when(mMockAccessToken.getToken()).thenReturn("");
        when(mMockAuthenticationService.getUserToken()).thenReturn(mMockAccessToken);
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

}