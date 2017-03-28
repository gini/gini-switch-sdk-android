package net.gini.tariffsdk.authentication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.gini.tariffsdk.network.AuthenticationApi;
import net.gini.tariffsdk.network.AuthenticationApiImpl;
import net.gini.tariffsdk.network.NetworkCallback;

import okhttp3.OkHttpClient;

public class AuthenticationService {

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static AuthenticationService mInstance = null;
    private static SessionToken mSessionToken = null;
    private final Context mContext;
    private final OkHttpClient mOkHttpClient;

    private AuthenticationService(final Context context, final String clientId,
            final String clientPw, final OkHttpClient okHttpClient) {

        mContext = context;
        mOkHttpClient = okHttpClient;

        AuthenticationApi api = new AuthenticationApiImpl(mOkHttpClient);
        api.requestSessionToken(clientId, clientPw,
                new NetworkCallback<SessionToken>() {
                    @Override
                    public void onError(final Exception e) {
                        Log.e("OH NOES", e.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(final SessionToken sessionToken) {
                        Log.d("HELLO", sessionToken.getToken());
                        mSessionToken = sessionToken;
                    }
                });
    }

    public static AuthenticationService getInstance(@NonNull final Context context,
            @NonNull final String clientId, @NonNull final String clientPw,
            final OkHttpClient okHttpClient) {
        if (mInstance == null) {
            mInstance = new AuthenticationService(context.getApplicationContext(), clientId,
                    clientPw, okHttpClient);
        }

        return mInstance;
    }

    @Nullable
    public static SessionToken getSessionToken() {
        return mSessionToken;
    }

}
