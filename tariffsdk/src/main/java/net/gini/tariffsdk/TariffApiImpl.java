package net.gini.tariffsdk;


import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.AuthenticationInterceptor;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.BearerAuthenticator;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class TariffApiImpl implements TariffApi {

    private final OkHttpClient mOkHttpClient;
    private final HttpUrl mTariffApiUrl;

    TariffApiImpl(final OkHttpClient okHttpClient,
            final AuthenticationService authenticationService) {
        this(okHttpClient, authenticationService, HttpUrl.parse(BuildConfig.TARIFF_API_URL));
    }

    @VisibleForTesting
    TariffApiImpl(final OkHttpClient okHttpClient,
            final AuthenticationService authenticationService, final HttpUrl url) {
        mTariffApiUrl = url;
        mOkHttpClient = okHttpClient.newBuilder()
                .authenticator(new BearerAuthenticator(authenticationService))
                .addInterceptor(new AuthenticationInterceptor(authenticationService))
                .build();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void requestConfiguration(@NonNull final NetworkCallback<Configuration> callback) {
        final HttpUrl url = mTariffApiUrl.newBuilder()
                .addEncodedPathSegment("TODO")
                .build();
        final Request request = createGetRequest(url);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final JSONObject object = new JSONObject(response.body().string());
                        final Configuration configuration = getConfigurationFromJson(object);
                        callback.onSuccess(configuration);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });
    }

    private Request createGetRequest(final HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Configuration getConfigurationFromJson(final JSONObject object) throws JSONException {

        long resolution = object.optLong("resolution");
        final int flashMode = object.optInt("flashmode");
        return new Configuration(resolution, flashMode);
    }
}
