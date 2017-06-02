package net.gini.tariffsdk.authentication;

import static junit.framework.Assert.assertEquals;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.tariffsdk.authentication.models.AccessToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BearerAuthenticatorTest {

    @Mock
    AccessToken mMockAccessToken;
    @Mock
    AuthenticationService mMockAuthenticationService;
    @Mock
    Callback mMockCallback;
    private MockWebServer mServer;

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();

        MockitoAnnotations.initMocks(this);
        when(mMockAccessToken.getToken()).thenReturn("token");
        when(mMockAuthenticationService.requestNewUserToken()).thenReturn(mMockAccessToken);
    }

    @Test
    public void shouldRemoveOldAuthorizationHeader() throws IOException,
            InterruptedException {

        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new BearerAuthenticator(mMockAuthenticationService))
                .build();

        final String authorizationHeader = "old authorization header";
        Request request = new Request.Builder()
                .addHeader("Authorization", authorizationHeader)
                .url(mServer.url("/")).build();

        mServer.enqueue(new MockResponse().setResponseCode(401));
        client.newCall(request).enqueue(mMockCallback);
        RecordedRequest firstRequest = mServer.takeRequest();
        RecordedRequest secondRequest = mServer.takeRequest();

        assertEquals(authorizationHeader, firstRequest.getHeader("Authorization"));
        assertNotEquals(authorizationHeader, secondRequest.getHeader("Authorization"));
    }

    @Test
    public void shouldRepeatSameRequestWithNewToken() throws IOException,
            InterruptedException {
        final String validToken = "valid-token";
        when(mMockAccessToken.getToken()).thenReturn(validToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new BearerAuthenticator(mMockAuthenticationService))
                .build();

        Request request = getDefaultRequest();

        mServer.enqueue(new MockResponse().setResponseCode(401));
        client.newCall(request).enqueue(mMockCallback);
        mServer.takeRequest();
        mServer.enqueue(new MockResponse().setResponseCode(200));
        RecordedRequest serverRequest = mServer.takeRequest();

        assertEquals("BEARER " + validToken, serverRequest.getHeader("Authorization"));
    }

    @Test
    public void shouldRequestClientTokenWhenUnauthorized() throws IOException,
            InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new BearerAuthenticator(mMockAuthenticationService))
                .build();

        Request request = getDefaultRequest();

        mServer.enqueue(new MockResponse().setResponseCode(401));
        client.newCall(request).enqueue(mMockCallback);
        mServer.takeRequest();
        //since this is async we want to be sure the client has enough time to handle the response.
        Thread.sleep(5);
        verify(mMockAuthenticationService).requestNewUserToken();
    }

    @Test
    public void shouldRequestNotMoreThanThreeTimesInErrorCase() throws IOException,
            InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator(new BearerAuthenticator(mMockAuthenticationService))
                .build();

        Request request = getDefaultRequest();

        mServer.enqueue(new MockResponse().setResponseCode(401));
        mServer.enqueue(new MockResponse().setResponseCode(401));
        mServer.enqueue(new MockResponse().setResponseCode(401));
        mServer.enqueue(new MockResponse().setResponseCode(401));
        client.newCall(request).enqueue(mMockCallback);
        mServer.takeRequest();
        mServer.takeRequest();
        mServer.takeRequest();
        int requestCount = mServer.getRequestCount();
        assertEquals(3, requestCount);
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    private Request getDefaultRequest() {
        return new Request.Builder()
                .url(mServer.url("/")).build();
    }

}