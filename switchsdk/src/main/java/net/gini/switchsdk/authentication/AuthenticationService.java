package net.gini.switchsdk.authentication;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.gini.switchsdk.authentication.models.AccessToken;
import net.gini.switchsdk.network.NetworkCallback;

import java.io.IOException;

public interface AuthenticationService {

    @Nullable
    AccessToken getUserToken();

    void init(@NonNull final NetworkCallback<Void> callback);

    void requestNewUserToken(@NonNull final NetworkCallback<AccessToken> callback);

    AccessToken requestNewUserToken() throws IOException;
}
