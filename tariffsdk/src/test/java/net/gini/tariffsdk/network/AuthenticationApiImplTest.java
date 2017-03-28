package net.gini.tariffsdk.network;

import static org.junit.Assert.assertEquals;

import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.jodah.concurrentunit.Waiter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AuthenticationApiImplTest {

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private Waiter mWaiter;

    @Test
    public void requestHeader_Accept() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestHeader_Accept");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken(getNoopCallback());

        RecordedRequest request = mServer.takeRequest();
        assertEquals(request.getHeader("Accept"), "application/json");
    }

    @Test
    public void requestHeader_ContentType() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestHeader_ContentType");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken(getNoopCallback());

        RecordedRequest request = mServer.takeRequest();
        assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
    }

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
        mWaiter = new Waiter();
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    @Test
    public void tokenResponse_receive_valid_token() throws InterruptedException, TimeoutException {

        final MockResponse mockResponse = new MockResponse().setBody(
                "{\"access_token\":\"1eb7ca49-d99f-40cb-b86d-8dd689ca2345\","
                        + "\"token_type\":\"bearer\",\"expires_in\":43199,\"scope\":\"read\"}");
        final AuthenticationApiImpl impl = getAuthenticationApi(mServer,
                mOkHttpClient, mockResponse);
        impl.requestSessionToken(new NetworkCallback<AccessToken>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
            }

            @Override
            public void onSuccess(final AccessToken accessToken) {
                mWaiter.assertEquals(accessToken.getToken(), "1eb7ca49-d99f-40cb-b86d-8dd689ca2345");
                mWaiter.resume();
            }
        });

        mServer.takeRequest();
        mWaiter.await();
    }

    @NonNull
    private static AuthenticationApiImpl getAuthenticationApi(final MockWebServer server,
            final OkHttpClient okHttpClient, final MockResponse requestHeader_accept) {
        server.enqueue(requestHeader_accept);
        final String url = server.url("/post").toString();
        final AuthenticationApiImpl authenticationApi = new AuthenticationApiImpl(
                new ClientCredentials("clientId", "clientSecret"), okHttpClient);
        authenticationApi.mUrl = url;
        return authenticationApi;
    }

    @NonNull
    private NetworkCallback<AccessToken> getNoopCallback() {
        return new NetworkCallback<AccessToken>() {
            @Override
            public void onError(final Exception e) {
            }

            @Override
            public void onSuccess(final AccessToken accessToken) {
            }
        };
    }
}