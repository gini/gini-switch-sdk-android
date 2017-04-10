package net.gini.tariffsdk.authentication;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.UserCredentials;
import net.gini.tariffsdk.authentication.user.UserManager;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.UserApi;

import org.json.JSONException;

import java.io.IOException;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserApi mUserApi;
    private final UserManager mUserManager;
    private volatile AccessToken mAccessToken;

    public AuthenticationServiceImpl(final @NonNull UserApi userApi, @NonNull final UserManager userManager) {

        mUserApi = userApi;

        mUserManager = userManager;
    }


    @Override
    public void init(@NonNull final NetworkCallback<Void> callback) {

        if(mUserManager.userCredentialsExist()) {
            requestUserToken(callback);
        } else {
            final UserCredentials userCredentials = mUserManager.getOrCreateUserCredentials();

            mUserApi.requestClientToken(new NetworkCallback<AccessToken>() {
                @Override
                public void onError(final Exception e) {
                    callback.onError(e);
                }

                @Override
                public void onSuccess(final AccessToken accessToken) {
                    createUser(accessToken, userCredentials);
                }

                private void createUser(final AccessToken accessToken,
                        final UserCredentials userCredentials) {
                    mUserApi.createUser(userCredentials, accessToken, new NetworkCallback<Void>() {
                        @Override
                        public void onError(final Exception e) {
                            callback.onError(e);
                        }

                        @Override
                        public void onSuccess(final Void aVoid) {
                            requestUserToken(callback);
                        }
                    });
                }
            });
        }
    }


    @Override
    public AccessToken getUserToken() {
        return mAccessToken;
    }

    @Override
    public void requestNewUserToken(@NonNull final NetworkCallback<AccessToken> callback) {
        mUserApi.requestUserToken(mUserManager.getOrCreateUserCredentials(),
                new NetworkCallback<AccessToken>() {
                    @Override
                    public void onError(final Exception e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(final AccessToken accessToken) {
                        mAccessToken = accessToken;
                        callback.onSuccess(accessToken);
                    }
                });
    }

    @Override
    public AccessToken requestNewUserToken() throws IOException {

        return mUserApi.requestUserTokenSync(mUserManager.getUserCredentials());
    }


    private void requestUserToken(final @NonNull NetworkCallback<Void> callback) {
        mUserApi.requestUserToken(mUserManager.getOrCreateUserCredentials(),
                new NetworkCallback<AccessToken>() {
                    @Override
                    public void onError(final Exception e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(final AccessToken accessToken) {
                        mAccessToken = accessToken;
                        callback.onSuccess(null);
                    }
                });
    }
}
