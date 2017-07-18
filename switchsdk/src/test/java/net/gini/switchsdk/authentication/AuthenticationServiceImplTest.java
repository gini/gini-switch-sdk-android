package net.gini.switchsdk.authentication;

import static junit.framework.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.switchsdk.authentication.models.AccessToken;
import net.gini.switchsdk.authentication.models.UserCredentials;
import net.gini.switchsdk.authentication.user.UserManager;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.UserApi;

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
    @Captor
    ArgumentCaptor<Exception> mExceptionCaptor;
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
        verify(mMockUserManager).getOrCreateUserCredentials();
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

        AccessToken accessToken = mock(AccessToken.class);
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

        verify(mMockVoidNetworkCallback).onSuccess(null);

    }

    @Test
    public void requestNewUserToken_FailedShouldCallOnError() throws Exception {

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        Exception exception = mock(Exception.class);
        authenticationService.requestNewUserToken(mMockAccessTokenNetworkCallback);


        verify(mMockUserApi).requestUserToken(eq(mMockUserCredentials),
                mNetworkCallbackCaptor.capture());

        mNetworkCallbackCaptor.getValue().onError(exception);

        verify(mMockAccessTokenNetworkCallback).onError(mExceptionCaptor.capture());

        Exception capturedException = mExceptionCaptor.getValue();
        assertEquals(exception, capturedException);
    }

    @Test
    public void requestNewUserToken_SuccessShouldGetAccessToken() throws Exception {

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        AccessToken accessToken = mock(AccessToken.class);
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

        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
                mMockUserApi, mMockUserManager);

        AccessToken accessToken = mock(AccessToken.class);
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
        when(mMockUserManager.getOrCreateUserCredentials()).thenReturn(mMockUserCredentials);
    }

}