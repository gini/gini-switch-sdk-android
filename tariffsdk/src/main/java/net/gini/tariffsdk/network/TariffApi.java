package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.configuration.models.ClientInformation;
import net.gini.tariffsdk.configuration.models.Configuration;

public interface TariffApi {

    void addPage(@NonNull final String pagesUrl, @NonNull final byte[] page,
            @NonNull final NetworkCallback<ExtractionOrderPage> callback);

    void createExtractionOrder(@NonNull final NetworkCallback<ExtractionOrder> callback);

    void getOrderState(@NonNull final String orderUrl,
            @NonNull final NetworkCallback<ExtractionOrderState> callback);

    void requestConfiguration(@NonNull final ClientInformation clientInformation,
            @NonNull final NetworkCallback<Configuration> callback);

}