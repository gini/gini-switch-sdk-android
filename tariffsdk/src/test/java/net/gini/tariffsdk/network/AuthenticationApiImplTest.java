package net.gini.tariffsdk.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.gini.tariffsdk.authentication.SessionToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AuthenticationApiImplTest {

    private MockWebServer mServer;

    @Test
    public void requestBody_clientId() throws InterruptedException {

        mServer.enqueue(new MockResponse().setBody("requestBody_clientId"));
        final String url = mServer.url("/post").toString();
        AuthenticationApiImpl impl = new AuthenticationApiImpl();
        impl.mUrl = url;

        impl.requestSessionToken("userId", "userPw",
                new AuthenticationApi.NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        assertEquals(e, null);
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                    }
                });

        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("username=userId"));
    }

    @Test
    public void requestBody_clientPassword() throws InterruptedException {

        mServer.enqueue(new MockResponse().setBody("requestBody_clientPassword"));
        final String url = mServer.url("/post").toString();
        AuthenticationApiImpl impl = new AuthenticationApiImpl();
        impl.mUrl = url;

        impl.requestSessionToken("userId", "userPw",
                new AuthenticationApi.NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        assertEquals(e, null);
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                    }
                });

        RecordedRequest request = mServer.takeRequest();
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("password=userPw"));
    }

    @Test
    public void requestHeader_Accept() throws InterruptedException {

        mServer.enqueue(new MockResponse().setBody("requestHeader_Accept"));
        final String url = mServer.url("/post").toString();
        AuthenticationApiImpl impl = new AuthenticationApiImpl();
        impl.mUrl = url;

        impl.requestSessionToken("userId", "userPw",
                new AuthenticationApi.NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        assertEquals(e, null);
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                    }
                });

        RecordedRequest request = mServer.takeRequest();
        assertEquals(request.getHeader("Accept"), "application/json");
    }

    @Test
    public void requestHeader_ContentType() throws InterruptedException {

        mServer.enqueue(new MockResponse().setBody("requestHeader_ContentType"));
        final String url = mServer.url("/post").toString();
        AuthenticationApiImpl impl = new AuthenticationApiImpl();
        impl.mUrl = url;

        impl.requestSessionToken("userId", "userPw",
                new AuthenticationApi.NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        assertEquals(e, null);
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                    }
                });

        RecordedRequest request = mServer.takeRequest();
        assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
    }

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }
}