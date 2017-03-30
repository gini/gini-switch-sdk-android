package net.gini.tariffsdk.authentication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.authentication.models.UserCredentials;
import net.gini.tariffsdk.authentication.user.UserManager;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.UserApiImpl;

import okhttp3.OkHttpClient;

public class AuthenticationServiceImpl implements AuthenticationService {

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static AuthenticationServiceImpl mInstance = null;
    private static AccessToken sAccessToken = null;
    private final UserApiImpl mApi;
    private final UserManager mUserManager;
    private final Context mContext;
    private final OkHttpClient mOkHttpClient;

    private AuthenticationServiceImpl(final Context context, final @NonNull ClientCredentials clientCredentials, final OkHttpClient okHttpClient, final UserManager userManager) {

        mContext = context;
        mOkHttpClient = okHttpClient;

        mApi = new UserApiImpl(clientCredentials, mOkHttpClient);

        mUserManager = userManager;
    }


    @Override
    public void init(@NonNull final NetworkCallback<Void> callback) {
        //1. check if user exists
        //if yes, request token
        //if no create user and then request token

        if(mUserManager.userCredentialsExist()) {
            requestUserToken(callback);
        } else {
            mApi.requestClientToken(new NetworkCallback<AccessToken>() {
                @Override
                public void onError(final Exception e) {
                    callback.onError(e);
                }

                @Override
                public void onSuccess(final AccessToken accessToken) {
                    UserCredentials userCredentials = mUserManager.getUserCredentials();
                    createUser(accessToken, userCredentials);
                }

                private void createUser(final AccessToken accessToken,
                        final UserCredentials userCredentials) {
                    mApi.createUser(userCredentials, accessToken, new NetworkCallback<Void>() {
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

    private void requestUserToken(final @NonNull NetworkCallback<Void> callback) {
        mApi.requestUserToken(mUserManager.getUserCredentials(),
                new NetworkCallback<AccessToken>() {
                    @Override
                    public void onError(final Exception e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onSuccess(final AccessToken accessToken) {
                        //TODO store access token
                        callback.onSuccess(null);
                    }
                });
    }
}
