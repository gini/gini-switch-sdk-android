package net.gini.tariffsdk.network;


import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.AuthenticationInterceptor;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.BearerAuthenticator;
import net.gini.tariffsdk.configuration.models.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class TariffApiImpl implements TariffApi {

    private final OkHttpClient mOkHttpClient;
    @VisibleForTesting
    String mTariffApiUrl = BuildConfig.TARIFF_API_URL;

    TariffApiImpl(final OkHttpClient okHttpClient,
            final AuthenticationService authenticationService) {
        mOkHttpClient = okHttpClient.newBuilder()
                .authenticator(new BearerAuthenticator(authenticationService))
                .addInterceptor(new AuthenticationInterceptor(authenticationService))
                .build();
    }

    @Override
    public void requestConfiguration(@NonNull final NetworkCallback<Configuration> callback) {
        final String url = mTariffApiUrl + "/TODO";
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

    private Request createGetRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Configuration getConfigurationFromJson(final JSONObject object) throws JSONException {

        long resolution = object.getLong("resolution");
        final int flashMode = object.getInt("flashmode");
        return new Configuration(resolution, flashMode);
    }
}
