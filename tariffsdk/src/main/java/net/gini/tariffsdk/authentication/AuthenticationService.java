package net.gini.tariffsdk.authentication;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.network.NetworkCallback;

public interface AuthenticationService {

    void init(@NonNull final NetworkCallback<Void> callback);

    @Nullable
    AccessToken getUserToken();

    void requestNewUserToken(@NonNull final NetworkCallback<AccessToken> callback);
}
