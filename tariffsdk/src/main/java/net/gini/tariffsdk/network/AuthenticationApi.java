package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;

public interface AuthenticationApi {

    void requestSessionToken(@NonNull final NetworkCallback<AccessToken> callback);

}
