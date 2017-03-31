package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.authentication.models.UserCredentials;

import java.io.IOException;

public interface UserApi {

    void requestClientToken(@NonNull final NetworkCallback<AccessToken> callback);

    void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback);


    void requestUserToken(@NonNull UserCredentials userCredentials, @NonNull final NetworkCallback<AccessToken> callback);

    AccessToken requestUserTokenSync(@NonNull UserCredentials userCredentials) throws IOException;

}
