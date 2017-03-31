package net.gini.tariffsdk.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.SessionToken;
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
    public void requestBody_clientId() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestBody_clientId");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken("userId", "userPw", getNoopCallback());

        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("username=userId"));
    }

    @Test
    public void requestBody_clientPassword() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestBody_clientPassword");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken("userId", "userPw", getNoopCallback());

        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("password=userPw"));
    }

    @Test
    public void requestHeader_Accept() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestHeader_Accept");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken("userId", "userPw", getNoopCallback());

        RecordedRequest request = mServer.takeRequest();
        assertEquals(request.getHeader("Accept"), "application/json");
    }

    @Test
    public void requestHeader_ContentType() throws InterruptedException {

        final MockResponse mockResponse = new MockResponse().setBody("requestHeader_ContentType");
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mServer, mOkHttpClient,
                mockResponse);
        authenticationApi.requestSessionToken("userId", "userPw", getNoopCallback());

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
                "{token:\"request_token_bla_blub\"}");
        final AuthenticationApiImpl impl = getAuthenticationApi(mServer,
                mOkHttpClient, mockResponse);
        impl.requestSessionToken("userId", "userPw",
                new AuthenticationApi.NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                        mWaiter.assertEquals(sessionToken.getToken(), "request_token_bla_blub");
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
        final AuthenticationApiImpl authenticationApi = new AuthenticationApiImpl(okHttpClient);
        authenticationApi.mUrl = url;
        return authenticationApi;
    }

    @NonNull
    private AuthenticationApi.NetworkCallback<SessionToken> getNoopCallback() {
        return new AuthenticationApi.NetworkCallback<SessionToken>() {
            @Override
            public void onError(final Exception e) {
            }

            @Override
            public void onSuccess(final SessionToken sessionToken) {
            }
        };
    }
}