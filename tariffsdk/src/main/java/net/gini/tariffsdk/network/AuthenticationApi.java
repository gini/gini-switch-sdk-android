package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.SessionToken;

public interface AuthenticationApi {

    void requestSessionToken(@NonNull final String clientId, @NonNull final String clientPw,
            @NonNull final NetworkCallback<SessionToken> callback);

}
