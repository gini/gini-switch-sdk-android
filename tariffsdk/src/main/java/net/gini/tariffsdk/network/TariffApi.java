package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.configuration.models.ClientParameter;
import net.gini.tariffsdk.configuration.models.Configuration;

public interface TariffApi {

    void requestConfiguration(@NonNull final ClientParameter clientParameter,
            @NonNull final NetworkCallback<Configuration> callback);

}
