package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.network.interceptors.AuthenticationInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthenticationApiImpl implements AuthenticationApi {

    private final OkHttpClient mHttpClient;

    @VisibleForTesting
    String mUrl = BuildConfig.BASE_URL + Constants.AUTHENTICATE_USER;

    public AuthenticationApiImpl(@NonNull final ClientCredentials clientCredentials,
            final OkHttpClient okHttpClient) {

        mHttpClient = okHttpClient.newBuilder()
                .addInterceptor(new AuthenticationInterceptor(clientCredentials))
                .build();
    }

    @Override
    public void requestSessionToken(@NonNull final NetworkCallback<AccessToken> callback) {

        final RequestBody bodyType = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(mUrl)
                .addHeader("Accept", "application/json")
                .post(bodyType)
                .build();

        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                int expiresIn;
                String scope;
                String token;
                String type;
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    expiresIn = obj.getInt("expires_in");
                    scope = obj.getString("scope");
                    token = obj.getString("access_token");
                    type = obj.getString("token_type");
                    AccessToken accessToken = new AccessToken(expiresIn, scope, token, type);
                    callback.onSuccess(accessToken);
                } catch (JSONException e) {
                    callback.onError(e);
                }


            }
        });
    }

}
