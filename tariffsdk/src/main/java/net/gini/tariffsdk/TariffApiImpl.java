package net.gini.tariffsdk;


import android.accounts.NetworkErrorException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.AuthenticationInterceptor;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.BearerAuthenticator;
import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.configuration.models.FlashMode;
import net.gini.tariffsdk.network.ExtractionOrder;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    @Override
    public void addPage(@NonNull final String pagesUrl, @NonNull final Uri uri,
            final NetworkCallback<Void> callback) {
        //TODO
    }

    @Override
    public void createExtractionOrder(@NonNull final NetworkCallback<ExtractionOrder> callback) {
        final HttpUrl url = mTariffApiUrl.newBuilder()
                .addPathSegment("extractionOrders")
                .build();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), "{ }");
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                //TODO
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        final JSONObject links = obj.getJSONObject("_links");
                        final String selfUrl = links.getJSONObject("self").getString("href");
                        final String pagesUrl = links.getJSONObject("pages").getString("href");

                        ExtractionOrder extractionOrder = new ExtractionOrder(selfUrl, pagesUrl);

                        callback.onSuccess(extractionOrder);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void requestConfiguration(@NonNull final ClientParameter clientParameter,
            @NonNull final NetworkCallback<Configuration> callback) {
        final HttpUrl url = mTariffApiUrl.newBuilder()
                .addEncodedPathSegment("config")
                .addQueryParameter(ClientParameter.PLATFORM_NAME, clientParameter.getPlatformName())
                .addQueryParameter(ClientParameter.OSVERSION_NAME, clientParameter.getOsVersion())
                .addQueryParameter(ClientParameter.DEVICE_NAME, clientParameter.getDeviceModel())
                .addQueryParameter(ClientParameter.SDKVERSION_NAME, clientParameter.getSdkVersion())
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

        final FlashMode flashMode = FlashMode.valueOf(
                object.optString(Configuration.FLASH_MODE, FlashMode.ON.name()));
        return new Configuration(flashMode);
    }
}
