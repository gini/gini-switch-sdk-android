package net.gini.tariffsdk;


import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.AuthenticationInterceptor;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.BearerAuthenticator;
import net.gini.tariffsdk.configuration.models.ClientInformation;
import net.gini.tariffsdk.configuration.models.Configuration;
import net.gini.tariffsdk.configuration.models.FlashMode;
import net.gini.tariffsdk.network.ExtractionOrder;
import net.gini.tariffsdk.network.ExtractionOrderPage;
import net.gini.tariffsdk.network.ExtractionOrderState;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
    public void addPage(@NonNull final String pagesUrl, @NonNull final byte[] page,
            @NonNull final NetworkCallback<ExtractionOrderPage> callback) {
        final HttpUrl url = HttpUrl.parse(pagesUrl);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                page);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/hal+json")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        ExtractionOrderPage page = createExtractionOrderPageFromJson(obj);
                        callback.onSuccess(page);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });

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
                        ExtractionOrder extractionOrder = createExtractionOrderFromJson(obj);

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

    @Override
    public void getOrderState(@NonNull final String orderUrl,
            @NonNull final NetworkCallback<ExtractionOrderState> callback) {
        Request request = createGetRequest(HttpUrl.parse(orderUrl));
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    final JSONObject obj;
                    try {
                        obj = new JSONObject(response.body().string());
                        ExtractionOrder extractionOrder = createExtractionOrderFromJson(obj);

                        ArrayList<ExtractionOrderPage> orderPages = new ArrayList<>();
                        JSONArray pagesJSONArray = obj.getJSONObject("_embedded").getJSONArray(
                                "pages");
                        for (int i = 0; i < pagesJSONArray.length(); i++) {
                            JSONObject jsonPage = pagesJSONArray.getJSONObject(i);
                            ExtractionOrderPage page = createExtractionOrderPageFromJson(jsonPage);
                            orderPages.add(page);
                        }


                        callback.onSuccess(new ExtractionOrderState(orderPages, extractionOrder));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    callback.onError(new NetworkErrorException("TODO SOME ERROR"));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void requestConfiguration(@NonNull final ClientInformation clientInformation,
            @NonNull final NetworkCallback<Configuration> callback) {
        final HttpUrl url = mTariffApiUrl.newBuilder()
                .addEncodedPathSegment("config")
                .addQueryParameter(ClientInformation.PLATFORM_NAME,
                        clientInformation.getPlatformName())
                .addQueryParameter(ClientInformation.OSVERSION_NAME,
                        clientInformation.getOsVersion())
                .addQueryParameter(ClientInformation.DEVICE_NAME,
                        clientInformation.getDeviceModel())
                .addQueryParameter(ClientInformation.SDKVERSION_NAME,
                        clientInformation.getSdkVersion())
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

    @NonNull
    private ExtractionOrder createExtractionOrderFromJson(final JSONObject obj)
            throws JSONException {
        final JSONObject links = obj.getJSONObject("_links");
        final String selfUrl = links.getJSONObject("self").getString("href");
        final String pagesUrl = links.getJSONObject("pages").getString("href");
        return new ExtractionOrder(selfUrl, pagesUrl);
    }

    @NonNull
    private ExtractionOrderPage createExtractionOrderPageFromJson(final JSONObject jsonPage)
            throws JSONException {
        final String self = jsonPage.getJSONObject("_links").getJSONObject("self").getString
                ("href");
        final ExtractionOrderPage.Status status =
                ExtractionOrderPage.Status.valueOf(jsonPage.getString("status"
                        + ""));
        return new ExtractionOrderPage(self, status);
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
