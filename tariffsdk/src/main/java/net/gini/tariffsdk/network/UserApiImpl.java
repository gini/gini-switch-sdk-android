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

public class UserApiImpl implements UserApi {

    @NonNull
    private final ClientCredentials mClientCredentials;
    private final OkHttpClient mHttpClient;

    @VisibleForTesting
    String mUrl = BuildConfig.USER_API_URL + Constants.AUTHENTICATE_USER;

    public UserApiImpl(@NonNull final ClientCredentials clientCredentials,
            final OkHttpClient okHttpClient) {
        mClientCredentials = clientCredentials;
        mHttpClient = okHttpClient;
    }

    @Override
    public void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback) {

        final String credentialsJson = createCredentialsJson(userCredentials);

        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                credentialsJson);

        final Request request = createPostRequest(accessToken, body);

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

        final Request request = createGetRequest();

        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        final AccessToken accessToken = getAccessToken(obj);
                        callback.onSuccess(accessToken);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });
    }

    private String createCredentialsJson(final @NonNull UserCredentials userCredentials) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", userCredentials.getEmail());
            jsonObject.put("password", userCredentials.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    private Request createGetRequest() {
        return new Request.Builder()
                .url(mUrl)
                .addHeader("Accept", "application/json")
                .addHeader(Constants.HEADER_NAME_AUTHORIZATION,
                        Credentials.basic(mClientCredentials.getClientId(),
                                mClientCredentials.getClientSecret()))
                .build();
    }

    private Request createPostRequest(final @NonNull AccessToken accessToken,
            final RequestBody body) {
        return new Request.Builder()
                .url(mUrl)
                .addHeader("Authorization", "BEARER " + accessToken.getToken())
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    @NonNull
    private AccessToken getAccessToken(final JSONObject jsonObject) throws JSONException {
        final int expiresIn = jsonObject.getInt("expires_in");
        final String scope = jsonObject.getString("scope");
        final String token = jsonObject.getString("access_token");
        final String type = jsonObject.getString("token_type");
        return new AccessToken(expiresIn, scope, token, type);
    }

}
