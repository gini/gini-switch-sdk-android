package net.gini.switchsdk;


import static net.gini.switchsdk.utils.SwitchException.ErrorCode.CREATE_EXTRACTION_ORDER;
import static net.gini.switchsdk.utils.SwitchException.ErrorCode.GET_ORDER_STATE;
import static net.gini.switchsdk.utils.SwitchException.ErrorCode.REQUEST_CONFIGURATION;
import static net.gini.switchsdk.utils.SwitchException.ErrorCode.RETRIEVE_EXTRACTIONS;
import static net.gini.switchsdk.utils.SwitchException.ErrorCode.UPLOAD_IMAGE;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

import net.gini.switchsdk.authentication.AuthenticationInterceptor;
import net.gini.switchsdk.authentication.AuthenticationService;
import net.gini.switchsdk.authentication.BearerAuthenticator;
import net.gini.switchsdk.configuration.models.ClientInformation;
import net.gini.switchsdk.configuration.models.Configuration;
import net.gini.switchsdk.configuration.models.FlashMode;
import net.gini.switchsdk.network.ExtractionOrder;
import net.gini.switchsdk.network.ExtractionOrderPage;
import net.gini.switchsdk.network.ExtractionOrderState;
import net.gini.switchsdk.network.Extractions;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.TariffApi;
import net.gini.switchsdk.utils.SwitchException;

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
                .addInterceptor(new AuthenticationInterceptor(authenticationService))
                .authenticator(new BearerAuthenticator(authenticationService))
                .build();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
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

        mOkHttpClient.newCall(request).enqueue(getPagesResponseCallback(callback));

    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
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
                callback.onError(new SwitchException(CREATE_EXTRACTION_ORDER, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        ExtractionOrder extractionOrder = createExtractionOrderFromJson(obj);

                        callback.onSuccess(extractionOrder);
                    } catch (JSONException e) {
                        callback.onError(
                                new SwitchException(CREATE_EXTRACTION_ORDER, e.getMessage()));
                    }
                } else {
                    callback.onError(new SwitchException(CREATE_EXTRACTION_ORDER));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void deletePage(@NonNull final String pagesUrl) {
        final HttpUrl url = HttpUrl.parse(pagesUrl);
        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                //TODO. retry mechanism maybe
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                //since fire and forget nothing to do here
            }
        });

    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void getOrderState(@NonNull final String orderUrl,
            @NonNull final NetworkCallback<ExtractionOrderState> callback) {
        Request request = createGetRequest(HttpUrl.parse(orderUrl));
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(GET_ORDER_STATE, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    final JSONObject obj;
                    try {
                        obj = new JSONObject(response.body().string());
                        ExtractionOrder extractionOrder = createExtractionOrderFromJson(obj);
                        final boolean orderComplete = getOrderCompleteStateFromJson(obj);

                        ArrayList<ExtractionOrderPage> orderPages = new ArrayList<>();
                        JSONArray pagesJSONArray = obj.getJSONObject("_embedded").getJSONArray(
                                "pages");
                        for (int i = 0; i < pagesJSONArray.length(); i++) {
                            JSONObject jsonPage = pagesJSONArray.getJSONObject(i);
                            ExtractionOrderPage page = createExtractionOrderPageFromJson(jsonPage);
                            orderPages.add(page);
                        }


                        final String extractionUrl = getExtractionsUrlFromJson(obj);
                        callback.onSuccess(new ExtractionOrderState(orderPages, extractionOrder,
                                orderComplete, extractionUrl));

                    } catch (JSONException e) {
                        callback.onError(new SwitchException(GET_ORDER_STATE, e.getMessage()));
                    }


                } else {
                    callback.onError(new SwitchException(GET_ORDER_STATE));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void replacePage(@NonNull final String pagesUrl, @NonNull final byte[] page,
            @NonNull final NetworkCallback<ExtractionOrderPage> callback) {
        final HttpUrl url = HttpUrl.parse(pagesUrl);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                page);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/hal+json")
                .put(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(getPagesResponseCallback(callback));
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
                callback.onError(new SwitchException(REQUEST_CONFIGURATION, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final JSONObject object = new JSONObject(response.body().string());
                        final Configuration configuration = getConfigurationFromJson(object);
                        callback.onSuccess(configuration);
                    } catch (JSONException e) {
                        callback.onError(
                                new SwitchException(REQUEST_CONFIGURATION, e.getMessage()));
                    }
                } else {
                    callback.onError(new SwitchException(REQUEST_CONFIGURATION));
                }
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void retrieveExtractions(@NonNull final String orderUrl,
            @NonNull final NetworkCallback<Extractions> callback) {
        Request request = createGetRequest(HttpUrl.parse(orderUrl));
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(RETRIEVE_EXTRACTIONS, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final JSONObject obj;
                    try {
                        obj = new JSONObject(response.body().string());
                        final String selfUrl = getSelfLink(obj);
                        final String companyName = obj.optJSONObject("companyName").optString(
                                "value");
                        final String energyMeterNumber = obj.optJSONObject(
                                "energyMeterNumber").optString("value");
                        JSONObject consumption = obj.optJSONObject("consumption");
                        double consumptionValue = 0;
                        String consumptionUnit = null;
                        if (consumption != null) {
                            JSONObject consumptionJsonObject = consumption.optJSONObject("value");
                            consumptionValue = consumptionJsonObject.optDouble(
                                    "value");
                            consumptionUnit = consumptionJsonObject.optString("unit");
                        }
                        callback.onSuccess(new Extractions(selfUrl, companyName, energyMeterNumber,
                                consumptionValue,
                                consumptionUnit));
                    } catch (JSONException e) {
                        callback.onError(new SwitchException(RETRIEVE_EXTRACTIONS, e.getMessage()));
                    }
                } else {
                    callback.onError(new SwitchException(RETRIEVE_EXTRACTIONS));
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
        final String self = getSelfLink(jsonPage);
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

    private String getExtractionsUrlFromJson(final JSONObject obj) {
        try {
            return obj.getJSONObject("_links").getJSONObject("extractions").getString("href");
        } catch (JSONException ignored) {
        }
        return null;
    }

    private boolean getOrderCompleteStateFromJson(final JSONObject obj) {
        try {
            return obj.getBoolean("extractionsComplete");
        } catch (JSONException ignored) {
        }
        return false;
    }

    @NonNull
    private Callback getPagesResponseCallback(
            final @NonNull NetworkCallback<ExtractionOrderPage> callback) {
        return new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                callback.onError(new SwitchException(UPLOAD_IMAGE, e.getMessage()));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final JSONObject obj = new JSONObject(response.body().string());
                        ExtractionOrderPage page = createExtractionOrderPageFromJson(obj);
                        callback.onSuccess(page);
                    } catch (JSONException e) {
                        callback.onError(new SwitchException(UPLOAD_IMAGE, e.getMessage()));
                    }
                } else {
                    callback.onError(new SwitchException(UPLOAD_IMAGE));
                }
            }
        };
    }

    private String getSelfLink(final JSONObject obj) throws JSONException {
        final JSONObject links = obj.getJSONObject("_links");
        return links.getJSONObject("self").getString("href");
    }
}
