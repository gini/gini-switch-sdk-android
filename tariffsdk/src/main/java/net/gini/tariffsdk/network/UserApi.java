package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.UserCredentials;

public interface UserApi {

    void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback);

    void requestClientToken(@NonNull final NetworkCallback<AccessToken> callback);

}
