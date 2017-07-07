package net.gini.tariffsdk;


import static net.gini.tariffsdk.utils.SwitchException.ErrorCode.CREATE_USER;
import static net.gini.tariffsdk.utils.SwitchException.ErrorCode.REQUEST_CLIENT_TOKEN;
import static net.gini.tariffsdk.utils.SwitchException.ErrorCode.REQUEST_USER_TOKEN;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.authentication.models.UserCredentials;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.UserApi;
import net.gini.tariffsdk.utils.Logging;
import net.gini.tariffsdk.utils.SwitchException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class UserApiImpl implements UserApi {

    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    private final HttpUrl mBaseUrl;
    @NonNull
    private final ClientCredentials mClientCredentials;
    private final OkHttpClient mHttpClient;

    public UserApiImpl(@NonNull final ClientCredentials clientCredentials,
            final OkHttpClient okHttpClient) {
        this(clientCredentials, okHttpClient, HttpUrl.parse(BuildConfig.USER_API_URL));
    }

    @VisibleForTesting
    UserApiImpl(@NonNull final ClientCredentials mockClientCredentials,
            final OkHttpClient okHttpClient, final HttpUrl url) {

        mClientCredentials = mockClientCredentials;
        mHttpClient = okHttpClient;
        mBaseUrl = url;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback) {

        final String credentialsJson;
        try {
            credentialsJson = createCredentialsJson(userCredentials);
        } catch (SwitchException e) {
            callback.onError(new SwitchException(CREATE_USER, e.getMessage()));
            return;
        }

        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                credentialsJson);

        final HttpUrl url = mBaseUrl.newBuilder()
                .addPathSegment("api")
                .addPathSegment("users")
                .build();
        final Request request = createPostRequest(accessToken, body, url);

        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(CREATE_USER, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new SwitchException(CREATE_USER));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void requestClientToken(@NonNull final NetworkCallback<AccessToken> callback) {
        final HttpUrl url = createTokenUrl("client_credentials");

        final Request request = createGetRequest(url);
        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(REQUEST_CLIENT_TOKEN, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response)
                    throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        final AccessToken accessToken = getAccessToken(obj);
                        callback.onSuccess(accessToken);
                    } catch (JSONException e) {
                        callback.onError(new SwitchException(REQUEST_CLIENT_TOKEN, e.getMessage()));
                    }
                } else {
                    callback.onError(new SwitchException(REQUEST_CLIENT_TOKEN));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public AccessToken requestNewUserTokenSync(@NonNull final UserCredentials userCredentials)
            throws IOException {

        final RequestBody requestBody = new FormBody.Builder()
                .add("username", userCredentials.getEmail())
                .add("password", userCredentials.getPassword())
                .build();

        final HttpUrl url = createTokenUrl("password");
        final Request request = createAuthenticatedPostRequest(requestBody, url);

        final Response response = mHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            final JSONObject obj;
            try {
                obj = new JSONObject(response.body().string());
                return getAccessToken(obj);
            } catch (JSONException ignored) {
            }
        }
        throw new IOException();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void requestUserToken(@NonNull final UserCredentials userCredentials,
            @NonNull final NetworkCallback<AccessToken> callback) {
        final RequestBody requestBody = new FormBody.Builder()
                .add("username", userCredentials.getEmail())
                .add("password", userCredentials.getPassword())

                .build();

        final HttpUrl url = createTokenUrl("password");

        final Request request = createAuthenticatedPostRequest(requestBody, url);

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(REQUEST_USER_TOKEN, e.getMessage()));
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
                    callback.onError(new SwitchException(REQUEST_USER_TOKEN));
                }
            }
        });
    }

    private Request createAuthenticatedPostRequest(final RequestBody body, final HttpUrl url) {
        final String credential = Credentials.basic(mClientCredentials.getClientId(),
                mClientCredentials.getClientSecret());
        return new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .header("Authorization", credential)
                .post(body)
                .build();
    }

    private String createCredentialsJson(final @NonNull UserCredentials userCredentials)
            throws SwitchException {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", userCredentials.getEmail());
            jsonObject.put("password", userCredentials.getPassword());
        } catch (JSONException e) {
            Logging.e("Could not create credentials.", e);
            throw new SwitchException(e.getMessage());
        }

        return jsonObject.toString();
    }

    private Request createGetRequest(final HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader(HEADER_NAME_AUTHORIZATION,
                        Credentials.basic(mClientCredentials.getClientId(),
                                mClientCredentials.getClientSecret()))
                .build();
    }

    private Request createPostRequest(final @NonNull AccessToken accessToken,
            final RequestBody body, final HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", "BEARER " + accessToken.getToken())
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    @NonNull
    private HttpUrl createTokenUrl(final String value) {
        return mBaseUrl.newBuilder()
                .addPathSegment("oauth")
                .addPathSegment("token")
                .addQueryParameter("grant_type", value)
                .build();
    }

    @NonNull
    private AccessToken getAccessToken(final JSONObject jsonObject) throws JSONException {
        final int expiresIn = jsonObject.getInt("expires_in");
        //TODO validate if scope is needed
        final String scope = jsonObject.optString("scope");
        final String token = jsonObject.getString("access_token");
        final String type = jsonObject.getString("token_type");
        return new AccessToken(expiresIn, scope, token, type);
    }

}
