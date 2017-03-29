package net.gini.tariffsdk.authentication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.network.UserApi;
import net.gini.tariffsdk.network.UserApiImpl;
import net.gini.tariffsdk.network.NetworkCallback;

import okhttp3.OkHttpClient;

public class AuthenticationService {

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static AuthenticationService mInstance = null;
    private static AccessToken sAccessToken = null;
    private final Context mContext;
    private final OkHttpClient mOkHttpClient;

    private AuthenticationService(final Context context, final @NonNull ClientCredentials clientCredentials, final OkHttpClient okHttpClient) {

        mContext = context;
        mOkHttpClient = okHttpClient;

        UserApi api = new UserApiImpl(clientCredentials, mOkHttpClient);
        api.requestClientToken(new NetworkCallback<AccessToken>() {
                    @Override
                    public void onError(final Exception e) {
                        Log.e("OH NOES", e.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(final AccessToken sessionToken) {
                        Log.d("HELLO", sessionToken.getToken());
                        sAccessToken = sessionToken;
                    }
                });
    }

    public static AuthenticationService getInstance(@NonNull final Context context,
            @NonNull final String clientId, @NonNull final String clientPw,
            final OkHttpClient okHttpClient) {
        if (mInstance == null) {
            mInstance = new AuthenticationService(context.getApplicationContext(), new ClientCredentials(clientId, clientPw), okHttpClient);
        }

        return mInstance;
    }

    @Nullable
    public static AccessToken getSessionToken() {
        return sAccessToken;
    }

}
