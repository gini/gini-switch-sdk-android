package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.SessionToken;

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
    String mUrl = "http://user.stage.gini.net/oauth/token?grant_type=password";

    public AuthenticationApiImpl(final OkHttpClient okHttpClient) {

        mHttpClient = okHttpClient.newBuilder()
                .build();
    }

    @Override
    public void requestSessionToken(@NonNull final String clientId, @NonNull final String clientPw,
            @NonNull final NetworkCallback<SessionToken> callback) {

        final RequestBody bodyType = new FormBody.Builder()
                .add("username", clientId)
                .add("password", clientPw)
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

                String token = "";
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    token = obj.getString("token");
                } catch (JSONException e) {
                    callback.onError(e);
                }

                SessionToken sessionToken = new SessionToken(token);

                callback.onSuccess(sessionToken);
            }
        });
    }
}
