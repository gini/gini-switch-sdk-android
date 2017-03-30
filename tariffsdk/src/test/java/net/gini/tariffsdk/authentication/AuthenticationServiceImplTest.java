package net.gini.tariffsdk.authentication;

import static junit.framework.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.UserCredentials;
import net.gini.tariffsdk.authentication.user.UserManager;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.UserApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthenticationServiceImplTest {

    @Captor
    ArgumentCaptor<AccessToken> mAccessTokenCaptor;
    @Mock
    NetworkCallback<AccessToken> mMockAccessTokenNetworkCallback;
    @Mock
    UserApi mMockUserApi;
    @Mock
    UserCredentials mMockUserCredentials;
    @Mock
    UserManager mMockUserManager;
    @Mock
    NetworkCallback<Void> mMockVoidNetworkCallback;
    @Captor
    ArgumentCaptor<NetworkCallback<AccessToken>> mNetworkCallbackCaptor;
    @Captor
    ArgumentCaptor<NetworkCallback<Void>> mNetworkCallbackVoidCaptor;

    @Test
    public void initFirstTime_ShouldCreateCredentials() throws Exception {
        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        authenticationService.init(mMockVoidNetworkCallback);
        verify(mMockUserManager).getUserCredentials();
    }

    @Test
    public void initFirstTime_ShouldRequestClientToken() throws Exception {
        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        authenticationService.init(mMockVoidNetworkCallback);

        verify(mMockUserApi).requestClientToken(Matchers.<NetworkCallback<AccessToken>>any());
    }

    @Test
    public void initSecondTime_ShouldRequestClientToken() throws Exception {


        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        AccessToken accessToken = new AccessToken(0, "", "", "");
        authenticationService.requestNewUserToken(mMockAccessTokenNetworkCallback);


        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onSuccess(accessToken);

        verify(mMockAccessTokenNetworkCallback).onSuccess(mAccessTokenCaptor.capture());

        AccessToken capturedAccessToken = mAccessTokenCaptor.getValue();
        assertEquals(accessToken, capturedAccessToken);
    }

    @Test
    public void initSecondTime_ShouldRequestUserToken() throws Exception {
        when(mMockUserManager.userCredentialsExist()).thenReturn(true);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        authenticationService.init(mMockVoidNetworkCallback);

        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onSuccess(any(AccessToken.class));

        verify(mMockVoidNetworkCallback).onSuccess(any(Void.class));

    }

    @Test
    public void requestNewUserToken_FailedShouldCallOnError() throws Exception {

        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        authenticationService.requestNewUserToken(mMockAccessTokenNetworkCallback);

        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onError(any(Exception.class));
        verify(mMockAccessTokenNetworkCallback, only()).onError(any(Exception.class));
    }

    @Test
    public void requestNewUserToken_SuccessShouldGetAccessToken() throws Exception {


        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        AccessToken accessToken = new AccessToken(0, "", "", "");
        authenticationService.requestNewUserToken(mMockAccessTokenNetworkCallback);


        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onSuccess(accessToken);

        verify(mMockAccessTokenNetworkCallback).onSuccess(mAccessTokenCaptor.capture());

        AccessToken capturedAccessToken = mAccessTokenCaptor.getValue();
        assertEquals(accessToken, capturedAccessToken);
    }

    @Test
    public void requestNewUserToken_SuccessShouldSToreAccessToken() throws Exception {


        when(mMockUserManager.userCredentialsExist()).thenReturn(false);

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        AccessToken accessToken = new AccessToken(0, "", "", "");
        authenticationService.requestNewUserToken(mMockAccessTokenNetworkCallback);


        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onSuccess(accessToken);

        assertEquals(accessToken, authenticationService.getUserToken());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mMockUserCredentials.getEmail()).thenReturn("user@email.com");
        when(mMockUserCredentials.getPassword()).thenReturn("password");
        when(mMockUserManager.getUserCredentials()).thenReturn(mMockUserCredentials);
    }

}