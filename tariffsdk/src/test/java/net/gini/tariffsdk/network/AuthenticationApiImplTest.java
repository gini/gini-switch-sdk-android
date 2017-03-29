package net.gini.tariffsdk.network;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.authentication.models.UserCredentials;
import net.jodah.concurrentunit.Waiter;

import org.json.JSONException;
import org.json.JSONObject;
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

public class AuthenticationApiImplTest {

    @Mock
    AccessToken mMockAccessToken;
    @Mock
    NetworkCallback<AccessToken> mMockAccessTokenNetworkCallback;
    @Mock
    ClientCredentials mMockClientCredentials;
    @Mock
    UserCredentials mMockUserCredentials;
    @Mock
    NetworkCallback<Void> mMockVoidNetworkCallback;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private MockWebServer mServer;
    private Waiter mWaiter;

    @Test
    public void createUser_shouldBePostMethod()
            throws InterruptedException, TimeoutException {
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);

        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }


    @Test
    public void createUser_shouldContainAnAcceptHeader() throws InterruptedException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("application/json", request.getHeader("Accept"));
    }

    @Test
    public void createUser_shouldContainClientEmailData()
            throws InterruptedException, JSONException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);

        final String email = "our_awesome@client.net";
        when(mMockUserCredentials.getEmail()).thenReturn(email);

        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        final RecordedRequest request = mServer.takeRequest();
        final String requestEmail = new JSONObject(request.getBody().readUtf8()).getString("email");
        assertEquals(email, requestEmail);
    }

    @Test
    public void createUser_shouldContainClientPasswordsData()
            throws InterruptedException, JSONException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);

        final String password = "super_secret_test_password";
        when(mMockUserCredentials.getPassword()).thenReturn(password);

        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        final RecordedRequest request = mServer.takeRequest();
        final String requestPassword = new JSONObject(request.getBody().readUtf8()).getString(
                "password");
        assertEquals(password, requestPassword);
    }

    @Test
    public void createUser_shouldContainContentTypeHeader() throws InterruptedException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("application/json; charset=utf-8", request.getHeader("Content-Type"));
    }

    @Test
    public void createUser_shouldContainTheAccessTokenAsAuthorization()
            throws InterruptedException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        final String accessToken = "1eb7ca49-d99f-40cb-b86d-8dd689ca2345";

        when(mMockAccessToken.getToken()).thenReturn(accessToken);
        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                mMockVoidNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("BEARER " + accessToken, request.getHeader("Authorization"));
    }

    @Test
    public void createUser_wasSuccessfulShouldCallOnError()
            throws InterruptedException, JSONException, TimeoutException {

        mServer.enqueue(new MockResponse().setResponseCode(500));
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);

        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                new NetworkCallback<Void>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.assertTrue(e instanceof NetworkErrorException);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Void aVoid) {
                        mWaiter.fail();
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void createUser_wasSuccessfulShouldCallOnSuccess()
            throws InterruptedException, JSONException, TimeoutException {

        MockResponse mJSONMockResponse = new MockResponse().setBody("{\"email\":\"some_user"
                + "@example.com\",\"password:\"supersecret\"}");
        mServer.enqueue(mJSONMockResponse);
        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);

        authenticationApi.createUser(mMockUserCredentials, mMockAccessToken,
                new NetworkCallback<Void>() {
                    @Override
                    public void onError(final Exception e) {
                        mWaiter.fail(e);
                        mWaiter.resume();
                    }

                    @Override
                    public void onSuccess(final Void aVoid) {
                        mWaiter.assertNull(aVoid);
                        mWaiter.resume();
                    }
                });

        mWaiter.await();
    }

    @Test
    public void requestClientToken_responseShouldContainAValidJSONWithToken()
            throws InterruptedException, TimeoutException {

        final MockResponse mockResponse = new MockResponse().setBody(
                "{\"access_token\":\"1eb7ca49-d99f-40cb-b86d-8dd689ca2345\","
                        + "\"token_type\":\"bearer\",\"expires_in\":43199,\"scope\":\"read\"}");
        mServer.enqueue(mockResponse);
        final AuthenticationApiImpl impl = getAuthenticationApi(mOkHttpClient);
        impl.requestClientToken(new NetworkCallback<AccessToken>() {
            @Override
            public void onError(final Exception e) {
                mWaiter.fail(e);
                mWaiter.resume();
            }

            @Override
            public void onSuccess(final AccessToken accessToken) {
                mWaiter.assertEquals(accessToken.getToken(),
                        "1eb7ca49-d99f-40cb-b86d-8dd689ca2345");
                mWaiter.resume();
            }
        });

        mWaiter.await();
    }

    @Test
    public void requestClientToken_shouldBeAGetRequest()
            throws InterruptedException, TimeoutException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        authenticationApi.requestClientToken(mMockAccessTokenNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void requestClientToken_shouldContainAnAcceptHeader() throws InterruptedException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        authenticationApi.requestClientToken(mMockAccessTokenNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("application/json", request.getHeader("Accept"));
    }

    @Test
    public void requestClientToken_shouldContainBasicAuthorization() throws InterruptedException {

        final AuthenticationApiImpl authenticationApi = getAuthenticationApi(mOkHttpClient);
        authenticationApi.requestClientToken(mMockAccessTokenNetworkCallback);

        RecordedRequest request = mServer.takeRequest();
        assertEquals("Basic Y2xpZW50SWQ6Y2xpZW50U2VjcmV0", request.getHeader("Authorization"));
    }

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer();
        mServer.start();
        mWaiter = new Waiter();

        MockitoAnnotations.initMocks(this);

        when(mMockAccessToken.getToken()).thenReturn("token");

        when(mMockUserCredentials.getEmail()).thenReturn("user@email.com");
        when(mMockUserCredentials.getPassword()).thenReturn("password");

        when(mMockClientCredentials.getClientId()).thenReturn("clientId");
        when(mMockClientCredentials.getClientSecret()).thenReturn("clientSecret");

    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    @NonNull
    private AuthenticationApiImpl getAuthenticationApi(final OkHttpClient okHttpClient) {
        final String url = mServer.url("/post").toString();
        final AuthenticationApiImpl authenticationApi = new AuthenticationApiImpl(
                mMockClientCredentials, okHttpClient);
        authenticationApi.mUrl = url;
        return authenticationApi;
    }
}