package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.AuthenticationInterceptor;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.configuration.models.Configuration;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class TariffApiImpl implements TariffApi {

    private final String mTariffApiUrl = BuildConfig.TARIFF_API_URL;

    private final OkHttpClient mOkHttpClient;
    private final AuthenticationService mAuthenticationService;

    TariffApiImpl(final OkHttpClient okHttpClient, final AuthenticationService authenticationService) {
        mOkHttpClient = okHttpClient.newBuilder()
                .addInterceptor(new AuthenticationInterceptor(authenticationService))
                .build();
        mAuthenticationService = authenticationService;
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
                final Configuration configuration = new Configuration();
                callback.onSuccess(configuration);
            }
        });
    }

    private Request createGetRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();
    }
}
