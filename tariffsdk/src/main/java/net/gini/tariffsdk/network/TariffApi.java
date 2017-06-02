package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.configuration.models.ClientInformation;
import net.gini.tariffsdk.configuration.models.Configuration;

public interface TariffApi {

    void requestConfiguration(@NonNull final ClientInformation clientInformation,
            @NonNull final NetworkCallback<Configuration> callback);

}
