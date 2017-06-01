package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;

public interface TariffApi {

    void addPage(@NonNull final String pagesUrl, @NonNull final byte[] page,
            NetworkCallback<ExtractionOrderPage> callback);

    void createExtractionOrder(@NonNull final NetworkCallback<ExtractionOrder> callback);

    void requestConfiguration(@NonNull final ClientParameter clientParameter,
            @NonNull final NetworkCallback<Configuration> callback);

}
