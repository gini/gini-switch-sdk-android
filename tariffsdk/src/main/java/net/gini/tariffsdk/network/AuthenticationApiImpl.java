package net.gini.tariffsdk.network;


import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.authentication.models.UserCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthenticationApiImpl implements AuthenticationApi {

    @NonNull
    private final ClientCredentials mClientCredentials;
    private final OkHttpClient mHttpClient;

    @VisibleForTesting
    String mUrl = BuildConfig.USER_API_URL + Constants.AUTHENTICATE_USER;

    public AuthenticationApiImpl(@NonNull final ClientCredentials clientCredentials,
            final OkHttpClient okHttpClient) {
        mClientCredentials = clientCredentials;

        mHttpClient = okHttpClient;
    }

    @Override
    public void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", userCredentials.getEmail());
            jsonObject.put("password", userCredentials.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString());

        final Request request = new Request.Builder()
                .url(mUrl)
                .addHeader("Authorization", "BEARER " + accessToken.getToken())
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });
    }

    @Override
    public void requestClientToken(@NonNull final NetworkCallback<AccessToken> callback) {

        Request request = new Request.Builder()
                .url(mUrl)
                .addHeader("Accept", "application/json")
                .addHeader(Constants.HEADER_NAME_AUTHORIZATION,
                        Credentials.basic(mClientCredentials.getClientId(),
                                mClientCredentials.getClientSecret()))
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
