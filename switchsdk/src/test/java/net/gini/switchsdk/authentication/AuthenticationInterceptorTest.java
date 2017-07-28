package net.gini.switchsdk.authentication;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import static org.mockito.Mockito.when;

import net.gini.switchsdk.authentication.models.AccessToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AuthenticationInterceptorTest {

    @Mock
    AccessToken mMockAccessToken;
    @Mock
    AuthenticationService mMockAuthenticationService;
    private MockWebServer mServer;

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
        mServer.enqueue(new MockResponse());

        MockitoAnnotations.initMocks(this);

        when(mMockAuthenticationService.getUserToken()).thenReturn(mMockAccessToken);
    }

    @Test
    public void shouldInterceptRequestWhenTokenIsAvailable()
            throws IOException, InterruptedException {

        final String validToken = "valid-token";
        when(mMockAccessToken.getToken()).thenReturn(validToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(mMockAuthenticationService))
                .build();
        Request request = getDefaultRequest();

        client.newCall(request).execute();

        RecordedRequest serverRequest = mServer.takeRequest();

        assertEquals("BEARER " + validToken, serverRequest.getHeader("Authorization"));

    }


    @Test
    public void shouldNotInterceptRequestWhenTokenIsNotAvailable()
            throws IOException, InterruptedException {

        when(mMockAuthenticationService.getUserToken()).thenReturn(null);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AuthenticationInterceptor(mMockAuthenticationService))
                .build();
        Request request = getDefaultRequest();

        client.newCall(request).execute();

        RecordedRequest serverRequest = mServer.takeRequest();

        assertNull(serverRequest.getHeader("Authorization"));
    }

    @Test
    public void shouldResponseWithUnauthorizedCodeWhenTokenIsNotAvailable()
            throws IOException, InterruptedException {

        when(mMockAuthenticationService.getUserToken()).thenReturn(null);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(mMockAuthenticationService))
                .build();
        Request request = getDefaultRequest();

        Response response = client.newCall(request).execute();

        assertEquals(401, response.code());
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